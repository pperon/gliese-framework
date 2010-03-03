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

package org.glieseframework.game;

import com.sun.sgs.app.ManagedObject;

import org.glieseframework.core.UserPlayer;

import org.glieseframework.game.Coordinate;

import org.glieseframework.message.Message;

import java.io.Serializable;


/**
 * Empty mode that is used by {@code BasicUserPlayer} for {@code UserPlayer}s
 * who are currently logged out.
 */
public class LoggedOutMode implements GameMode, Serializable, ManagedObject {

    private static final long serialVersionUID = 1;

    public String getName() {
        return "LoggedOutMode";
    }

    /** Note that handle is ignored and may therefore be null. */
    public GameProxy join(UserPlayer player, LeaveNotificationHandle handle) {
        return new LoggedOutProxy();
    }

    static class LoggedOutProxy implements GameProxy, Serializable {
        private static final long serialVersionUID = 1;
        static final EventResponse ACCEPT = new AcceptResponse();
        public void handleMessage(Message message) {}
        public EventResponse handleEvent(Event event) { return ACCEPT; }
        public void loggedOut() {}
        static final class AcceptResponse implements EventResponse {
            public boolean eventAccepted() { return true; }
        }
    }

}
