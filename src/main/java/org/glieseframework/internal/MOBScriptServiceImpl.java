/*
 * Copyright (c) 2009, Sun Microsystems, Inc.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in
 *       the documentation and/or other materials provided with the
 *       distribution.
 *     * Neither the name of Sun Microsystems, Inc. nor the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.glieseframework.internal;

import com.sun.sgs.app.ExceptionRetryStatus;

import com.sun.sgs.kernel.ComponentRegistry;
import com.sun.sgs.kernel.KernelRunnable;
import com.sun.sgs.kernel.TransactionScheduler;

import com.sun.sgs.service.TransactionProxy;

import org.glieseframework.mob.MOBPlayer;

import org.glieseframework.game.Coordinate;

import org.glieseframework.message.common.impl.MovementMessageImpl;

import java.io.File;
import java.io.FileReader;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;


/**  */
public class MOBScriptServiceImpl implements MOBScriptService {

    public static final String ENGINE_EXTENSION_PROPERTY =
        MOBScriptService.class.getName() + ".engine.extension";
    public static final String DEFAULT_ENGINE_EXTENSION = ".js";

    public static final String SCRIPT_DIR_PROPERTY =
        MOBScriptService.class.getName() + ".script.dir";
    public static final String DEFAULT_SCRIPT_DIR = "mobscripts";

    // this simpler structure can be used for thread-capable engines
    private final Map<String,CompiledScript> scriptMap =
        new HashMap<String,CompiledScript>();

    /*

      If your engine doesn't support multiple threads then you need
      to use the following implementation...the code should be updated
      to query the engine type and dynamically choose the approach.

    public static final int NUM_ENGINES = 4;
    private final Queue<Map<String,CompiledScript>> availableScriptMaps =
        new ConcurrentLinkedQueue<Map<String,CompiledScript>>();
    private final ThreadLocal<Map<String,CompiledScript>> localScriptMap =
        new ThreadLocal<Map<String,CompiledScript>>() {
            @Override protected Map<String,CompiledScript> initialValue() {
                Map<String,CompiledScript> map = availableScriptMaps.poll();
                if (map == null) {
                    // TODO: pool structure if thread count could change
                    throw new IllegalStateException("Insufficient Maps!");
                }
                return map;
            }
    };
    */

    // the local MOBPlayer that invoked a script
    private static final ThreadLocal<MOBPlayer> localPlayer =
        new ThreadLocal<MOBPlayer>();

    /**  */
    public MOBScriptServiceImpl(Properties props, ComponentRegistry registry,
                                TransactionProxy proxy)
    {
        String extension = props.getProperty(ENGINE_EXTENSION_PROPERTY,
                                             DEFAULT_ENGINE_EXTENSION);

        String scriptDirName = props.getProperty(SCRIPT_DIR_PROPERTY);
        if (scriptDirName == null) {
            scriptDirName = props.getProperty("com.sun.sgs.app.root") +
                File.separator + DEFAULT_SCRIPT_DIR;
        }
        File scriptDir = new File(scriptDirName);
        if (! scriptDir.exists()) {
            throw new IllegalArgumentException("No such directory: " +
                                               scriptDirName);
        }
        if (! scriptDir.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " +
                                               scriptDirName);
        }
        if ((! scriptDir.canRead()) || (! scriptDir.canExecute())) {
            throw new IllegalStateException("Insufficient permissions: " +
                                            scriptDirName);
        }

        // the following code works for engines that are multi-threaded
        ScriptEngineManager mgr = new ScriptEngineManager();
        String fileExtension =
            extension.startsWith(".") ? extension : "." + extension;
        Compilable c = getCompilableEngine(mgr, extension);
        loadScripts(scriptDir, c, fileExtension, scriptMap);

        /*
          If the script engine isn't multi-threaded, then use the following
          code to "prime" all the scheduler threads with a copy of the scripts

        TransactionScheduler scheduler =
            registry.getComponent(TransactionScheduler.class);
        for (int i = 0; i < NUM_ENGINES * 3; i++) {
            scheduler.scheduleTask(new EngineSetupTask(extension, scriptDir),
                                   proxy.getCurrentOwner());
        }
        */
    }

    /*
      This runnable is used to make sure that each of the threads in the
      scheduler has pre-loaded the set of scripts...note that this only
      works in the current code because the scheduler pool is fixed-size

    private final class EngineSetupTask implements KernelRunnable {
        private final String extension;
        private final File scriptDir;
        private EngineSetupTask(String extension, File scriptDir) {
            this.extension = extension;
            this.scriptDir = scriptDir;
        }
        public String getBaseTaskType() {
            return EngineSetupTask.class.getName();
        }
        public void run() throws Exception {
            if (localScriptMap.get() == null) {
                ScriptEngineManager mgr = new ScriptEngineManager();
                Compilable c = getCompilableEngine(mgr, extension);
                Map<String,CompiledScript> map =
                    new HashMap<String,CompiledScript>();
                String fileExtension = extension.startsWith(".") ?
                    extension : "." + extension;
                loadScripts(scriptDir, c, fileExtension, map);
                localScriptMap.set(map);
            }
        }
    }
    */

    /** Private method used to load a Compilable ScriptEngine by extension. */
    private Compilable getCompilableEngine(ScriptEngineManager mgr,
                                           String extension)
    {
        ScriptEngine engine = mgr.getEngineByExtension(extension);
        if (engine == null) {
            throw new NullPointerException("No such engine: " + extension);
        }
        if (! (engine instanceof Compilable)) {
            throw new IllegalStateException("Engine not Compilable: " +
                                            engine.get(ScriptEngine.ENGINE));
        }
        return (Compilable) engine;
    }

    /** Private method to load all scripts in and below a given directory. */
    private final void loadScripts(File dir, Compilable engine,
                                   String scriptExtension,
                                   Map<String,CompiledScript> map)
    {
        final String root = dir.getAbsolutePath() + File.separator;
        final int extLen = scriptExtension.length();
        for (String fileName : dir.list()) {
            File file = new File(root + fileName);
            if (file.isDirectory()) {
                if ((! file.canRead()) || (! file.canExecute())) {
                    System.err.println("Skipping directory: " + fileName);
                } else {
                    loadScripts(file, engine, scriptExtension, map);
                }
            } else if (fileName.endsWith(scriptExtension)) {
                if (file.canRead()) {
                    try {
                        CompiledScript script =
                            engine.compile(new FileReader(file));
                        String name =
                            fileName.substring(0, fileName.length() - extLen);
                        if (map.put(name, script) != null) {
                            System.err.println("duplicate script: " + name);
                        } else {
                            System.out.println("loaded: " + name);
                        }
                    } catch (Exception e) {
                        System.err.println("Failed to load script: " +
                                           fileName + ": " + e.getMessage());
                    }
                } else {
                    System.err.println("Skipping: " + fileName);
                }
            }
        }
    }

    /* Implement Service */

    /** {@inheritDoc} */
    public String getName() {
        return MOBScriptService.class.getName();
    }

    /** {@inheritDoc} */
    public void ready() { }

    /** {@inheritDoc} */
    public void shutdown() { }

    /* Implement MOBScriptService */

    /** {@inheritDoc} */
    public void callScript(String scriptName, MOBPlayer player /* ?? */) {
        try {
            localPlayer.set(player);
            getScript(scriptName).eval();
        } catch (Exception e) {
            throw new ScriptEngineException("masking threading issue", e);
        } finally {
            localPlayer.remove();
        }
    }

    /* Static methods that can be called from most scripts. */

    /** Example method that moves the calling MOBPlayer. */
    public static void move(float x, float y, float z, float speed) {
        MOBPlayer player = localPlayer.get();
        MovementMessageImpl message =
            new MovementMessageImpl(new Coordinate(x, y, z), speed, player);
        player.send(message);
    }

    /** Example method that stops the calling MOBPlayer */
    public static void stop() {
        localPlayer.get().stop();
    }

    /* Private utilities. */

    /** Utility to lookup a script by name. */
    private CompiledScript getScript(String name) {
        // use the following line instead for non-threaded engines
        //CompiledScript script = localScriptMap.get().get(name);
        // used for the threaded-engine code path
        CompiledScript script = scriptMap.get(name);
        if (script == null) {
            throw new IllegalArgumentException("Unknown script: " + name);
        }
        return script;
    }

    /** Wrapper exception to handle script engine failures. */
    private static class ScriptEngineException
        extends RuntimeException implements ExceptionRetryStatus
    {
        ScriptEngineException(String message) {
            super(message);
        }
        ScriptEngineException(String message, Throwable cause) {
            super(message, cause);
        }
        public boolean shouldRetry() {
            return true;
        }
    }

}
