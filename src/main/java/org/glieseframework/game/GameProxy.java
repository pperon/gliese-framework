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

import org.glieseframework.game.Coordinate;

import org.glieseframework.message.Message;


/** 
 * A proxy for interacting with a {@code Game} or {@code GameMode}. An
 * implementation is typically associated with a {@code Player}, returned
 * when a {@code Player} joins some mode, and so all calls are relative
 * to the associated {@code Player}. This proxy will also be used to
 * handle any incoming messages from associated clients.
 * <p>
 * All implementations must implement {@code Serializable} and may
 * optionally implement {@code ManagedObject}. The latter should not
 * be done unless the proxy needs to be shared, has mutable state, or
 * containts a large amount of state (which should be avoided).
 */
public interface GameProxy {

    /**
     * Handles an incoming message. For a {@code UserPlayer} this will be
     * the method that handles all messages from the associated client. This
     * is also the point to handle messages between {@code Player}s on the
     * server.
     * <p>
     * TODO: if the difference between a message and an event is that a
     * message comes from a player whereas an event comes from game or
     * region logic, then messages should have senders, right?
     */
    void handleMessage(Message message);

    /**
     * Handles some game-related event that comes from the server-side logic
     * and not a {@code Player}. The game needs to decide how to respond to
     * this event.
     *
     * @param event the {@code Event}
     *
     * @return the game's reaction to this event
     */
    EventResponse handleEvent(Event event);

    /**
     * Are there a few specific events that should be called out like this?
     * Should this just be an event on handleEvent()? This used to be release()
     * to invalidate the handle, but we don't generally want everyone calling
     * that so loggedOut() at least makes it clear when anyone except core
     * game logic calls this invalidation method.
     * <p>
     * TODO: this should definitely just be an event, not a specific
     * method, since {@code MOBPLayer}s will have proxies that never have
     * to handle this event.
     */
    void loggedOut();

}
