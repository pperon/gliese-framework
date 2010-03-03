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

package org.glieseframework.message.common.impl;

import org.glieseframework.game.Coordinate;

import org.glieseframework.core.Entity;

import org.glieseframework.message.common.MovementMessage;

import java.io.Serializable;

import java.nio.ByteBuffer;


/** A basic, shared implementation of {@code MovementMessage}. */
public class MovementMessageImpl extends AbstractMessage
    implements MovementMessage, Serializable
{

    private static final long serialVersionUID = 1;

    // the encoded form of the message, kept transiently so that server
    // code can use this message object across transactions
    private transient ByteBuffer encodedForm = null;

    // the movement location and speed
    private final Coordinate location;
    private final float speed;

    /**
     * Creates an instance of {@code MovementMessageImpl} for a move to
     * the given location with no specified speed.
     *
     * @param location the target location
     */
    public MovementMessageImpl(Coordinate location, Entity sender) {
        this(location, 0, sender);
    }

    /**
     * Creates an instance of {@code MovementMessageImpl} for a move to
     * the given location with the specified speed.
     *
     * @param location the target location
     * @param speed the movement speed
     */
    public MovementMessageImpl(Coordinate location, float speed,
                               Entity sender)
    {
        super(STANDARD_ID, sender);
        this.location = location;
        this.speed = speed;
    }

    /**
     * Creates an instance of {@code MovementMessageImpl} based on its
     * encoded form. This buffer must not contain the message identifier
     * as its leading bytes.
     *
     * @param messageBuffer the encoded form of a {@code MovementMessageImpl}
     */
    public MovementMessageImpl(ByteBuffer messageBuffer) {
        super(STANDARD_ID, parseSender(messageBuffer));
        this.location = getCoordinate(messageBuffer);
        this.speed = messageBuffer.getFloat();
    }

    /**  */
    public MovementMessageImpl(ByteBuffer messageBuffer, Entity sender) {
        super(STANDARD_ID, sender);
        this.location = getCoordinate(messageBuffer);
        this.speed = messageBuffer.getFloat();
    }

    private static Coordinate getCoordinate(ByteBuffer messageBuffer) {
        float x = messageBuffer.getFloat();
        float y = messageBuffer.getFloat();
        float z = messageBuffer.getFloat();
        return new Coordinate(x, y, z);
    }

    /* Implement Message */

    /** {@inheritDoc} */
    public ByteBuffer encodeMessage() {
        if (encodedForm != null) {
            return encodedForm;
        }
        encodedForm = ByteBuffer.allocate(18);
        encodedForm.putShort(getMessageId());
        encodedForm.putFloat(location.x);
        encodedForm.putFloat(location.y);
        encodedForm.putFloat(location.z);
        encodedForm.putFloat(speed);
        return encodedForm;
    }

    /* Implement MovementMessage */

    /** {@inheritDoc} */
    public Coordinate getLocation() {
        return location;
    }

    /** {@inheritDoc} */
    public float getSpeed() {
        return speed;
    }

}
