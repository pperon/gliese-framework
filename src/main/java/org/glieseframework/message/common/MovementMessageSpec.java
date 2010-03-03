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

package org.glieseframework.message.common;

import org.glieseframework.message.MessageSpec;

import org.glieseframework.game.Coordinate;

import org.glieseframework.message.common.MovementMessage;


/**
 * A common representation of the state associated with a
 * {@code MovementMessage}. This is typically used with the
 * {@code MessageManager} when the system has been configured to use
 * {@code MovementMessageHandler} to handle movement messages, but more
 * generally this specification class can be used with other handlers
 * as needed.
 */
public class MovementMessageSpec implements MessageSpec {

    // the target location and speed of the movement
    private final Coordinate location;
    private final float speed;

    /**
     * Creates an instance of {@code MovementMessageSpec}.
     *
     * @param location the target location of the movement
     * @param speed the speed of the movement
     */
    public MovementMessageSpec(Coordinate location, float speed) {
        this.location = location;
        this.speed = speed;
    }

    /* Implement MessageSpec. */

    /** {@inheritDoc} */
    public short getMessageId() {
        return MovementMessage.STANDARD_ID;
    }

    /* Accessor methods. */

    /**
     * Returns the target location of the movement.
     *
     * @return the target location
     */
    public Coordinate getLocation() {
        return location;
    }

    /**
     * Returns the speed of the movement.
     *
     * @return the movement speed
     */
    public float getSpeed() {
        return speed;
    }

}
