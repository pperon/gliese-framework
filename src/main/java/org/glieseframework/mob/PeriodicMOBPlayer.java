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
import com.sun.sgs.app.PeriodicTaskHandle;
import com.sun.sgs.app.RunWithNewIdentity;
import com.sun.sgs.app.Task;

import org.glieseframework.core.ObjectWrapper;

import org.glieseframework.game.GameProxy;

import java.io.Serializable;


/** A {@code ReactiveMOBPlayer} that also has a regular turn interval. */
public class PeriodicMOBPlayer extends ReactiveMOBPlayer {

    private static final long serialVersionUID = 1;

    private final long moveInterval;

    private PeriodicTaskHandle taskHandle = null;

    /**  */
    public PeriodicMOBPlayer(String name, GameProxy proxy, long moveInterval,
                             PeriodicMoveListener listener)
    {
        super(name, proxy, listener);
        this.moveInterval = moveInterval;
    }

    /* Override ReactiveMOBPlayer start() and stop() methods */

    public void start() {
        super.start();
        if (taskHandle == null) {
            PeriodicMoveListener listener =
                (PeriodicMoveListener) getListener();
            taskHandle = AppContext.getTaskManager().
                schedulePeriodicTask(new PeriodicMoveTask(listener),
                                     0L, moveInterval);
        }
    }

    public void stop() {
        super.stop();
        if (taskHandle != null) {
            taskHandle.cancel();
            taskHandle = null;
        }
    }
    
    /**  */
    @RunWithNewIdentity
    private static final class PeriodicMoveTask implements Task, Serializable {
        private static final long serialVersionUID = 1;
        private final ObjectWrapper<? extends PeriodicMoveListener>
            wrappedListener;
        PeriodicMoveTask(PeriodicMoveListener listener) {
            wrappedListener = new ObjectWrapper<PeriodicMoveListener>(listener);
        }
        public void run() {
            wrappedListener.get().nextMove();
        }
    }

}
