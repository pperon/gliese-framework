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

package org.glieseframework.core;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedObject;
import com.sun.sgs.app.ManagedReference;

import org.glieseframework.message.MessageManager;

import org.glieseframework.message.Message;

import java.io.Serializable;

import java.nio.ByteBuffer;


/**
 * Implementation of {@code Group} that allows the server to send to the
 * group, and optionally allows a single sender from the group also to send
 * messages to the group.
 */
public class SingleSenderGroup extends ChannelGroup implements Serializable {

    private static final long serialVersionUID = 1;

    /**  */
    public SingleSenderGroup(String groupName, boolean sendUpdates) {
        super(groupName, new SingleSenderFilter(null), sendUpdates);
    }

    /**  */
    public SingleSenderGroup(String groupName, boolean sendUpdates,
                             Player allowedSender)
    {
        super(groupName, new SingleSenderFilter(allowedSender), sendUpdates);
    }

    /** Private implementation of {@code GroupFilter}. */
    private static class SingleSenderFilter
        implements GroupFilter, Serializable
    {
        private static final long serialVersionUID = 1;
        // the optional single client sender allowed
        private final String senderName;
        /** Create an instance of {@code SingleSenderFilter}. */
        SingleSenderFilter(Player allowedSender) {
            this.senderName = allowedSender == null ? null :
                allowedSender.getName();
        }
        /** {@inheritDoc} */
        public Message filterMessage(ByteBuffer message, String sender) {
            if (senderName == null) {
                return null;
            }
            if (! senderName.equals(sender)) {
                return null;
            }
            return AppContext.getManager(MessageManager.class).
                decodeMessage(message);
        }
    }

}
