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

import org.glieseframework.core.UserPlayer;


/**
 * This interface represents the root logic of a slipstream game. When
 * the Darkstar application first starts, it will construct a single
 * {@code Game} that drives all game-specific logic. All implementations
 * of {@code Game} must also implement Serializable, and may implement
 * {@code ManagedObject}. It is strongly suggested that implementations
 * be immutable and contain fairly minimal state directly, and that
 * {@code ManagedObject} should only be implemented if mutable state
 * must be included.
 * <p>
 * All implementations must have a constructor that takes two parameters:
 * {@code Properties} and {@code RegionFactory}. This is how the {@code Game}
 * is initialized. The constructor is invoked in the context of an
 * unbounded transaction, so that it may take as much time as it needs
 * to setup the game.
 * <p>
 * An implementation represents top-level game-specific logic, and provides
 * access to all {@code UserPlayer} instances thereby getting to decide
 * what specific implementation of {@code UserPlayer} is used. It may have
 * its own state, but more likely a {@code Game} will be made up of many
 * {@code GameMode}s, and will do little more than create these modes and
 * handle moving {@code UserPlayer}s between the modes.
 */
public interface Game {

    /** Returns the {@code UserPlayer} with the given name. */
    UserPlayer getUserPlayer(String name);

    /**
     * Used when a user enters or re-enters the game (e.g., on login),
     * typically joining the player to a specific {@code GameMode}.
     * <p>
     * TODO: this should almost certainly have a different name. reJoin?
     * enterGame? startPlaying?
     */
    GameProxy join(UserPlayer player);

}
