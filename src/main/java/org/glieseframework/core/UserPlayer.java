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

package org.glieseframework.core;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.ObjectNotFoundException;

import org.glieseframework.game.Game;

import org.glieseframework.message.Message;

import java.io.Serializable;


/** Abstract implementation of {@code Player} for all users (i.e., clients). */
public abstract class UserPlayer implements Player, Serializable {

    private static final long serialVersionUID = 1;

    // the client's session
    private ManagedReference<? extends ClientSession> sessionRef;

    /**  */
    protected UserPlayer() {}

    /** Call to disconnect the client. */
    public final boolean disconnect() {
        // make sure that we're actually logged in
        if (getSession() == null) {
            return false;
        }
        AppContext.getDataManager().markForUpdate(this);
        sessionRef = null;
        loggedOut();
        return true;
    }

    /** Methods that extending classes must implement */

    /**  */
    public abstract void loggedIn(Game game);

    /**  */
    public abstract void loggedOut();

    /** Partial implementation of Player */

    /** {@inheritDoc} */
    public void send(Message message) {
        ClientSession session = getSession();
        if (session == null) {
            // TODO: decide what happens in this case
            return;
        }
        session.send(message.encodeMessage());
    }

    /** Package private methods to handle the session */

    /** Assign the current session for this player. */
    void setSession(ClientSession session) {
        AppContext.getDataManager().markForUpdate(this);
        sessionRef = session == null ?
            null : AppContext.getDataManager().createReference(session);
    }

    /** Gets the current session for this player, or null if not connected. */
    ClientSession getSession() {
        try {
            return sessionRef == null ? null : sessionRef.get();
        } catch (ObjectNotFoundException onfe) {
            // the system logged out the client and this is the first that
            // we've heard about it
            AppContext.getDataManager().markForUpdate(this);
            sessionRef = null;
            return null;
        }
    }

}
