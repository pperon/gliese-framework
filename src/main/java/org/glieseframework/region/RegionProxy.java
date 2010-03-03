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

import org.glieseframework.message.Message;

import java.util.Collection;


/**
 * The proxy that is used to interact with a {@code Region}. When some
 * entity (a {@code Player} or {@code item}) joins a {@code Region} it
 * gets back an instance of {@code RegionProxy}. All calls on that
 * proxy are relative to the entity, so for instance a call to
 * {@code getLocation} returns the location of the associated entity.
 * <p>
 * TODO: should the {@code ManagedObject} part be optional? Will there ever
 * be mulitple parties sharing the same proxy? As with {@code GameProxy},
 * it should probably be up to the implementation whether or not this is a
 * {@code ManagedObject}.
 */
public interface RegionProxy extends ManagedObject {

    /** Tell the proxy that the associated entity is leaving. */
    void release();
    
    /**  */
    Coordinate getLocation();
    
    /**  */
    boolean move(Coordinate location);

    /** Optional ability to move along a vector. */
    boolean move(Coordinate location, float speed);

    /**  */
    void sendMessage(Message message, float radius);
    
    /** TODO: is "visible" a detail of the region, game, or both? */
    void sendToVisible(Message message);

    /**  */
    Collection<? extends Player> getPlayers(float radius);

    /**  */
    Collection<? extends Item> getItems(float radius);

    /**  */
    Region getRegion();

}
