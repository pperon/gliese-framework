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

import org.glieseframework.internal.MessageHandler;

import org.glieseframework.message.MessageSpec;

import org.glieseframework.message.Message;

import org.glieseframework.message.common.impl.ChatMessageImpl;

import java.nio.ByteBuffer;


/**
 * A {@code MessageHandler} that supports {@code ChatMessage}s with the
 * common implementation {@code ChatMessageImpl}.
 * <p>
 * TODO: when sender details are added to the manager, they need to be used.
 */
public class ChatMessageHandler implements MessageHandler {

    /** Creates an instance of {@code ChatMessageHandler}. */
    public ChatMessageHandler() { }

    /* Implement MessageHandler. */

    /** {@inheritDoc} */
    public Message createMessage(MessageSpec messageSpec) {
        if (messageSpec.getMessageId() != ChatMessageImpl.STANDARD_ID) {
            throw new IllegalArgumentException("Can't create message type");
        }
        return new ChatMessageImpl(((ChatMessageSpec) messageSpec).
                                   getChatText(), null);
    }

    /** {@inheritDoc} */
    public Message decodeMessage(short messageId, ByteBuffer messageBuffer) {
        if (messageId != ChatMessageImpl.STANDARD_ID) {
            throw new IllegalArgumentException("Can't decode message type");
        }
        return new ChatMessageImpl(messageBuffer, null);
    }

}
