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

import org.glieseframework.core.Entity;

import org.glieseframework.message.common.ModeChangeMessage;

import java.io.Serializable;

import java.nio.ByteBuffer;


/** A basic, shared implementation of {@code ModeChangeMessage}. */
public class ModeChangeMessageImpl extends AbstractMessage
    implements ModeChangeMessage, Serializable
{

    private static final long serialVersionUID = 1;

    // the encoded form of the message, kept transiently so that server
    // code can use this message object across transactions
    private transient ByteBuffer encodedForm = null;

    // the name of the new mode
    private final String modeName;

    /**
     * Creates an instance of {@code ModeChangeMessageImpl} with the mode name.
     *
     * @param the name of the new game mode
     * @param sender the sender of the message or {@code null} if no sender
     *               is identified
     */
    public ModeChangeMessageImpl(String modeName, Entity sender) {
        super(STANDARD_ID, sender);
        this.modeName = modeName;
    }

    /**
     * Creates an instance of {@code ModeChangeMessageImpl} based on its
     * encoded form. This buffer must not contain the message identifier.
     *
     * @param messageBuffer the encoded form of a {@code ModeChangeMessageImpl}
     */
    public ModeChangeMessageImpl(ByteBuffer messageBuffer) {
        this(messageBuffer, parseSender(messageBuffer));
    }

    /**
     *
     */
    public ModeChangeMessageImpl(ByteBuffer messageBuffer, Entity sender) {
        super(STANDARD_ID, sender);
        byte [] modeBytes = new byte[messageBuffer.remaining()];
        messageBuffer.get(modeBytes);
        this.modeName = new String(modeBytes);
    }

    /* Implement Message */

    /** {@inheritDoc} */
    public ByteBuffer encodeMessage() {
        if (encodedForm != null) {
        	 return encodedForm;
        }
        byte [] modeBytes = modeName.getBytes();
        encodedForm = setupBuffer((short) modeBytes.length);
        encodedForm.put(modeBytes);
        encodedForm.position(0);
        return encodedForm;
    }

    /* Implement ModeChangeMessage */

    /** {@inheritDoc} */
    public String getModeName() {
        return modeName;
    }
}
