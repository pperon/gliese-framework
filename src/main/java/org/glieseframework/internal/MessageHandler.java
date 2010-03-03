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

package org.glieseframework.internal;

import org.glieseframework.message.MessageSpec;

import org.glieseframework.message.Message;

import java.nio.ByteBuffer;


/**
 * An interface used to define classes that know how to handle the creation
 * and decoding of specific types of messages. These are provided at startup
 * to the {@code MessageService} so that it knows how to handle messages of
 * specific types.
 * <p>
 * Implementations that will be used with the {@code MessageService} must
 * have a public no-argument constructor for the Service to create the
 * handler instance correctly.
 */
public interface MessageHandler {

    /**
     * Creates a {@code Message} from the given specification. If the
     * specification is not for a message type that this handler supports
     * then {@code IllegalArgumentException} is thrown.
     *
     * @param messageSpec the specification of a message
     *
     * @return a new {@code Message} based on the specification
     */
    Message createMessage(MessageSpec messageSpec);

    /**
     * Decodes the given buffer as a message of the given type. If the
     * message identifier is unknown to this handler, or if the buffer
     * is invalid then {@code IllegalArgumentException} is typically
     * thrown.
     * <p>
     * Note that the buffer should not contain the message identifier
     * in its contents. In a typical Slipstream message the first two
     * bytes represent the message identifier. So, the buffer here
     * is usually a Slipstream encoded message with the first two
     * bytes already removed and instead represented by the provided
     * message identifier.
     *
     * @param messageId the identifier for this message type
     * @param messageBuffer the encoded message
     *
     * @return a new {@code Message} parsed from the given buffer
     */
    Message decodeMessage(short messageId, ByteBuffer messageBuffer);

}
