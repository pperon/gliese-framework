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

import org.glieseframework.core.Player;
import org.glieseframework.core.UserPlayer;


/**
 * This interface defines a specific mode of play within a game. Typical
 * examples would be a lobby, a field for combat, an instance of a
 * card game, an aution house, etc. A {@code UserPlayer} may only be
 * actively part of a single {@code GameMode} at any one time, though
 * a {@code GameMode} could in turn tie together separate modes. A
 * {@code UserPlayer}'s active mode will define how any messages from
 * the client are processed, and how that player interacts with all
 * other players in the same mode.
 * <p>
 * Typically a {@code GameMode} will represent some common aspect of play
 * and interaction. In the case of a mode that has some notion of spacial,
 * physical interaction it will contain one or more {@code Region}s to
 * manage these interactions. A {@code GameMode} does not, however, have
 * to use any {@code Region} (for instance, a lobby or match-making mode
 * may not have any notion of interaction except for chat).
 */
public interface GameMode {

    /** The name of this mode, which must be unique. */
    String getName();

    /** TODO: this should optionally take some coordinates (etc.). */
    GameProxy join(UserPlayer player, LeaveNotificationHandle handle);

    // TODO: maybe some stats or membership details?

}
