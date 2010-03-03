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

import org.glieseframework.core.Entity;

import java.nio.ByteBuffer;


/**
 * The interface that is used to define all messages. A message is generally
 * anything communicated from one {@code Player} to another, to a
 * {@code Group}, or to the application logic. Connected clients send
 * messages that their server-side {@code GameProxy}s handle.
 * {@code MOBPlayer}s send and recieve messages like any other {@code Player},
 * except that these messages typically don't need to be encoded or
 * decoded since they are not passed over the network.
 * <p>
 * On the server-side, {@code Message}s can be created through the
 * {@code MessageManager}, which handles both decoding a message from its
 * encoded form and creating a message from an abstract specification.
 */
public interface Message {

    /**
     * Encodes the message contents into a form that can be sent over
     * the network. In Slipstream, all encoded messages must use the first
     * two bytes of their encoded form to specify the message identifier,
     * so the buffer returned from this method must start with a short
     * that is the same value returned from a call to {@code getMessageId}.
     *
     * @return the encoded message
     */
    ByteBuffer encodeMessage();

    /**
     * The message identifier.
     *
     * @return the message identifier
     */
    short getMessageId();

    /**
     * Returns a representation of the sender of this message. Messages do
     * not have to include the sender.
     *
     * @return the {@code Entity} that sent the message or {@code null} if
     *         the sender is not identified
     */
    Entity getSender();

}
