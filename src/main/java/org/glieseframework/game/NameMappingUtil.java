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

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.DataManager;
import com.sun.sgs.app.NameNotBoundException;
import com.sun.sgs.app.ObjectNotFoundException;

import org.glieseframework.core.UserPlayer;


/**
 * A utility class with static methods for name-based user mapping. This
 * provides one very simple way for a {@code Game} to manage its
 * instances of {@code UserPlayer}.
 */
public class NameMappingUtil {

    // map all users to a known namespace
    private static final String NAME_PREFIX = "slipstream:player:";

    /** Returns the player, or null if there is no player instance. */
    public static UserPlayer getUserPlayer(String name) {
        try {
            return (UserPlayer) AppContext.getDataManager().
                getBinding(NAME_PREFIX + name);
        } catch (NameNotBoundException nnbe) {
            return null;
        } catch (ObjectNotFoundException onfe) {
            return null;
        }
    }

    /** Adds a player to the set of known players. */
    public static void addUserPlayer(UserPlayer player) {
        String name = player.getName();
        if (getUserPlayer(name) != null) {
            throw new IllegalArgumentException("Player already exists: " +
                                               name);
        }
        AppContext.getDataManager().setBinding(NAME_PREFIX + name, player);
    }

}
