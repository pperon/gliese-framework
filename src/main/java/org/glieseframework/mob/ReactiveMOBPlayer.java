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

package org.glieseframework.mob;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.Task;

import org.glieseframework.core.Item;
import org.glieseframework.core.ObjectWrapper;

import org.glieseframework.game.GameProxy;

import org.glieseframework.message.Message;

import java.io.Serializable;

import java.util.Collection;


/** A basic MOB that takes a listener used to react to messages. */
public class ReactiveMOBPlayer implements MOBPlayer, Serializable {

    private static final long serialVersionUID = 1;

    private final String name;
    private final long id;

    private ObjectWrapper<? extends GameProxy> gameProxyWrapper;
    private ObjectWrapper<? extends MessageListener> listenerWrapper;

    private short delayVersion = 0;

    private boolean running = false;

    /**  */
    public ReactiveMOBPlayer(String name, GameProxy proxy,
                             MessageListener listener)
    {
        this.name = name;
        id = AppContext.getDataManager().getObjectId(this).longValue();
        gameProxyWrapper = new ObjectWrapper<GameProxy>(proxy);
        listenerWrapper = new ObjectWrapper<MessageListener>(listener);
    }

    /* Implement Player */

    public final String getName() {
        return name;
    }

    public final long getId() {
        return id;
    }

    public final void send(Message message) {
        if (running) {
            listenerWrapper.get().messageArrived(this, message);
        }
    }

    public final GameProxy getGameProxy() {
        return gameProxyWrapper.get();
    }

    public final boolean wantsToHearMessages() {
        return true;
    }

    public Collection<? extends Item> getItems() {
        throw new UnsupportedOperationException("No item support yet");
    }

    /* Implement MOBPlayer */

    public void start() {
        if (! running) {
            AppContext.getDataManager().markForUpdate(this);
            running = true;
        }
    }

    /**  */
    public void start(long delay) {
        if (! running) {
            AppContext.getTaskManager().
                scheduleTask(new DelayStartTask(this, delayVersion), delay);
        }
    }

    /**  */
    public void stop() {
        AppContext.getDataManager().markForUpdate(this);
        delayVersion++;
        running = false;
    }

    /* Protected utilities */

    protected MessageListener getListener() {
        return listenerWrapper.get();
    }

    /**  */
    private static final class DelayStartTask implements Task, Serializable {
        private static final long serialVersionUID = 1;
        private final ManagedReference<ReactiveMOBPlayer> playerRef;
        private final short version;
        DelayStartTask(ReactiveMOBPlayer player, short version) {
            playerRef = AppContext.getDataManager().createReference(player);
            this.version = version;
        }
        public void run() {
            ReactiveMOBPlayer player = playerRef.get();
            if (player.delayVersion == version) {
                playerRef.get().start();
            }
        }
    }

}
