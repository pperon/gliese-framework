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

package org.glieseframework.region;

import com.sun.sgs.app.ManagedObject;

import org.glieseframework.core.Item;
import org.glieseframework.core.Player;

import org.glieseframework.game.Coordinate;


/**
 * This interface defines an abstract space where {@code Player}s reside.
 * A typical example is a two-dimensional open space, but implementations
 * could be created for any kind of area where a {@code Player} can be.
 * The general idea is that a {@code Player} can join or leave a
 * {@code Region}, and while joined to a {@code Region} can try to
 * move around or interact with other {@code Player}s and {@code Item}s
 * in the {@code Region}. A {@code Region} implementation should manage
 * the legality of moves, handle updating {@code Player}s as movement
 * or state changes happen, etc., but delegate to the {@code GameMode}
 * that created it events that are part of the game-specific logic (e.g.
 * what happens, if anything, on collision, results of stepping on
 * specific spaces, etc.).
 * <p>
 * TODO: Should there be proxy for player to better lookup region details?
 */
public interface Region extends ManagedObject {

    /**  */
    RegionProxy join(Player player, Coordinate location);

    /**  */
    RegionProxy join(Region region, Coordinate location);

    /**  */
    RegionProxy join(Item item, Coordinate location);

}
