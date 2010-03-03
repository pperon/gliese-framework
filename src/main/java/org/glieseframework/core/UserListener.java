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
import com.sun.sgs.app.ClientSessionListener;
import com.sun.sgs.app.ManagedReference;

import org.glieseframework.message.MessageManager;

import java.io.Serializable;

import java.nio.ByteBuffer;


/**
 * A concrete, package-private class that enforces the proxy pattern for
 * incoming messages. Instances are created for a given {@code UserPlayer},
 * and that {@code UserPlayer}'s current {@code GameProxy} is used to
 * handle incoming messages. The {@code UserPlayer} is notified on logout.
 */
final class UserListener implements ClientSessionListener, Serializable {

    private static final long serialVersionUID = 1;

    // the user player sending us messages
    private final ManagedReference<? extends UserPlayer> playerRef;

    /** Create an instance of {@code UserListener} for the given player. */
    UserListener(UserPlayer player) {
        playerRef = AppContext.getDataManager().createReference(player);
    }
    
    /** {@inheritDoc} */
    public void receivedMessage(ByteBuffer message) {
        Player player = playerRef.get();
        // TODO: maybe here we get the message and check if it's a directed
        // message, in which case it gets handed off to a different interface?
        // Or dropped if no such handler is in place?
        player.getGameProxy().
            handleMessage(AppContext.getManager(MessageManager.class).
                          decodeMessage(message));
    }

    /** {@inheritDoc} */
    public void disconnected(boolean graceful) {
        UserPlayer player = playerRef.get();
        // TODO: should we explicitly call the current proxy's loggedOut()
        // method here, instead of making the player implementation do it?
        player.loggedOut();
        player.setSession(null);
    }

}
