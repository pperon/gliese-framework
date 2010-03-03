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

import com.sun.sgs.app.ManagedObject;

import org.glieseframework.game.GameProxy;

import org.glieseframework.core.Entity;

import org.glieseframework.message.Message;

import java.util.Collection;


/**
 * The core interface for any active entity in the system. These typically
 * will be {@code UserPlayer}s or {@code MOBPlayer}s, though any kind of
 * {@code Player} can be defined. All implementations must also implement
 * {@code Serializable} and override {@code equals} and {@code hashCode}.
 */
public interface Player extends ManagedObject, Entity {

    /** The unique name of this {@code Player} */
    String getName();

    /**  */
    long getId();

    /**
     * TODO: Should this return a boolean or throw an exception for the cases
     * where a message can't be sent? (e.g., a disconnected UserPlayer)
     * Should this include the sender?
     */
    void send(Message message);

    /** The current proxy for this {@code Player}. */
    GameProxy getGameProxy();

    /** TODO: Does this make sense? better way to opt-out of messages? */
    boolean wantsToHearMessages();

    /** TODO: is this really a generic aspect of all {@code Player}s? */
    Collection<? extends Item> getItems();

    // generic statistics blob?
    // generic status/state/news/etc.?

}
