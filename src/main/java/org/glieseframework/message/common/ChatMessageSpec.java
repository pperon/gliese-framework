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

import org.glieseframework.message.common.ChatMessage;


/**
 * A common representation of the state associated with a {@code ChatMessage}.
 * This is typically used with the {@code MessageManager} when the system has
 * been configured to use {@code ChatMessageHandler} to handle chat messages,
 * but more generally this specification class can be used with other
 * handlers as needed.
 */
public class ChatMessageSpec implements MessageSpec {

    // the chat text
    private final String chatText;

    /**
     * Creates an instance of {@code ChatMessageSpec}.
     *
     * @param chatText the text of the chat message
     */
    public ChatMessageSpec(String chatText) {
        this.chatText = chatText;
    }

    /* Implement MessageSpec. */

    /** {@inheritDoc} */
    public short getMessageId() {
        return ChatMessage.STANDARD_ID;
    }

    /* Accessor methods. */

    /**
     * Returns the text of the chat message.
     *
     * @return the text of the chat message
     */
    public String getChatText() {
        return chatText;
    }

}
