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

import org.glieseframework.game.GameProxy;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedObject;
import com.sun.sgs.app.ManagedObjectRemoval;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.RunWithNewIdentity;
import com.sun.sgs.app.Task;

import org.glieseframework.core.ObjectWrapper;

import org.glieseframework.game.GameProxy;

import java.io.Serializable;


/** A {@code ReactiveMOBPlayer} with an irregular turn interval. */
public class IrregularMOBPlayer extends ReactiveMOBPlayer
    implements ManagedObjectRemoval
{
    private static final long serialVersionUID = 1;

    private final ManagedReference<IrregularMoveTask> taskRef;

    private boolean running = false;

    /**  */
    public IrregularMOBPlayer(String name, GameProxy proxy,
                              IrregularMoveListener listener)
    {
        super(name, proxy, listener);
        taskRef = AppContext.getDataManager().
            createReference(new IrregularMoveTask(this, listener));
    }

    /* Implement ManagedObjectRemoval */

    public void removingObject() {
        AppContext.getDataManager().removeObject(taskRef.get());
    }

    /* Override ReactiveMOBPlayer start() and stop() methods */

    public void start() {
        super.start();
        IrregularMoveTask task = taskRef.get();
        if (task.stopped) {
            AppContext.getDataManager().markForUpdate(task);
            task.stopped = false;
            AppContext.getTaskManager().scheduleTask(task);
        }
    }

    public void stop() {
        super.stop();
        taskRef.getForUpdate().stopped = true;
    }

    /** FIXME: the new identity needs to be only on the first task. */
    @RunWithNewIdentity
    private static final class IrregularMoveTask
        implements Task, Serializable, ManagedObject
    {
        private static final long serialVersionUID = 1;
        private final ManagedReference<IrregularMOBPlayer> playerRef;
        private final ObjectWrapper<? extends IrregularMoveListener>
                                              wrappedListener;
        boolean stopped = true;
        IrregularMoveTask(IrregularMOBPlayer player,
                          IrregularMoveListener listener)
        {
            playerRef = AppContext.getDataManager().createReference(player);
            wrappedListener =
                new ObjectWrapper<IrregularMoveListener>(listener);
        }
        public void run() {
            if (! stopped) {
                long nextDelay = wrappedListener.get().nextMove();
                if (nextDelay != 0) {
                    AppContext.getTaskManager().scheduleTask(this, nextDelay);
                } else {
                    playerRef.get().stop();
                }
            }
        }
    }

}
