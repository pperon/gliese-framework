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

package org.glieseframework.message;

import org.glieseframework.message.Message;

import java.nio.ByteBuffer;


/**
 * A {@code Manager} that handles message encoding and decoding. This helps
 * separate the details of message formats on the wire from message usage
 * in application code. The idea is to make testing, node configuration,
 * networking changes (etc.) much easier. Note that this functionality, and
 * especially the underlying Service, is still being fleshed out.
 * <p>
 * TODO: both methods need to have an optional sender. In the case of
 * decodeMessage() the possibly embedded sender can't be trusted since this
 * is coming from a client. So, the sender needs to be included as another
 * paramater. For createMessage() it probably makes sense to include the
 * sender as part of a message's specification.
 */
public interface MessageManager {

    /**
     * Creates a {@code Message} based on the given specification. Throws
     * {@code IllegalArgumentException} if the message type cannot be
     * handled.
     *
     * @param messageSpec the specification of the message to create
     *
     * @return a new {@code Message} based on the specification
     */
    Message createMessage(MessageSpec messageSpec);

    /**
     * Decodes the given buffer and creates the appropriate type of
     * {@code Message}. Throws {@code IllegalArgumentException} if the
     * buffer contains a message type that can't be handled, or if the
     * buffer format cannot be decoded.
     *
     * @param messageBuffer the encoded message, where the first two
     *                      bytes must represent the message type
     *
     * @return a new {@code Message} decoded from the given buffer
     */
    Message decodeMessage(ByteBuffer messageBuffer);

}
