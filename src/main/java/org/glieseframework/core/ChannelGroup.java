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
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ChannelListener;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.Delivery;
import com.sun.sgs.app.ManagedObject;
import com.sun.sgs.app.ManagedReference;

import com.sun.sgs.app.util.ScalableHashSet;

import org.glieseframework.message.Message;

import java.io.Serializable;

import java.nio.ByteBuffer;

import java.util.HashSet;
import java.util.Set;


/**
 * Abstract utility implementation of {@code Group} that maintains a single
 * {@code Channel} for all client communcations. Note that any registered
 * listeners are always notified when {@code send} is called, but not when
 * a message arrives on a channel. In these cases, it's up to the specific
 * implementation to provide a {@code GroupFilter} that specifically
 * notifies the listeners (i.e., the non-{@code UserPlayer} members) if
 * this behavior is desired.
 */
abstract class ChannelGroup implements Group, Serializable {

    private static final long serialVersionUID = 1;

    // reference to the channel used by this group
    private final ManagedReference<? extends Channel> channelRef;

    // the set of non-UserPlayer group members
    private final ManagedReference<? extends Set<
        ManagedReference<Player>>> listenerSetRef;

    // whether or not to broadcast membership updates
    private final boolean sendUpdates;

    /**
     * Creates an instance of {@code ChannelGroup} with the given name and
     * optional filter. If no filter is provided then all messages are
     * sent directly to the {@code UserPlayer}s in the group. All messages
     * are sent reliably. If {@code sendUpdates} is {@code true} then all
     * group joins and leaves are broadcast to all listening members, and
     * all new arrivals are sent the complete group membership.
     */
    ChannelGroup(String groupName, GroupFilter filter, boolean sendUpdates) {
        this(groupName, filter, Delivery.RELIABLE, sendUpdates);
    }

    /**
     * Creates an instance of {@code ChannelGroup} with the given name,
     * optional filter and given delivery level.
     */
    ChannelGroup(String groupName, GroupFilter filter, Delivery delivery,
                 boolean sendUpdates)
    {
        GroupChannelListener listener = filter == null ? null :
            new GroupChannelListener(filter, this);
        Channel channel = AppContext.getChannelManager().
            createChannel(groupName, listener, delivery);
        channelRef = AppContext.getDataManager().createReference(channel);
        this.sendUpdates = sendUpdates;
        // if we're sending updates then this must be a small group, so just
        // embed a HashSet rather than using a scalable collection
        if (sendUpdates) {
            listenerSetRef = AppContext.getDataManager().
                createReference(new ManagedHashSet());
        } else {
            listenerSetRef = AppContext.getDataManager().
                createReference(new ScalableHashSet
                                <ManagedReference<Player>>());
        }
    }

    /** Implement Group */

    /** {@inheritDoc} */
    public String getName() {
        return channelRef.get().getName();
    }

    /** {@inheritDoc} */
    public void join(Player player) {
        if (sendUpdates) {
            // TODO: broadcast the player joined message
        }
        if (! player.wantsToHearMessages()) {
            return;
        }
        if (player instanceof UserPlayer) {
            channelRef.get().join(((UserPlayer) player).getSession());
        } else {
            listenerSetRef.get().
                add(AppContext.getDataManager().createReference(player));
        }
        if (sendUpdates) {
            // TODO: send the player the membership list
        }
    }

    /** {@inheritDoc} */
    public void leave(Player player) {
        if (sendUpdates) {
            // TODO: send the player left message
        }
        if (player instanceof UserPlayer) {
            channelRef.get().leave(((UserPlayer) player).getSession());
        } else {
            listenerSetRef.get().
                remove(AppContext.getDataManager().createReference(player));
        }
    }

    /** {@inheritDoc} */
    public void send(Message message) {
        // TODO: we could name the sender if this is included in the message,
        // but first we'd have to check if the sender is a client or a MOB,
        // since a MOB message would get rejected by the channel. Is there any
        // real value in identifying the sender here?
        channelRef.get().send(null, message.encodeMessage());
        sendToListeners(message);
    }

    /** Implement equality */

    /** {@inheritDoc} */
    public boolean equals(Object o) {
        return (o instanceof ChannelGroup) &&
            getName().equals(((ChannelGroup)o).getName());
    }

    /** {@inheritDoc} */
    public int hashCode() {
        return getName().hashCode();
    }

    /** Package private utilties */

    private static final class ManagedHashSet
        extends HashSet<ManagedReference<Player>>
        implements Serializable, ManagedObject
    {
        private static final long serialVersionUID = 1;
    }

    /** Sends to all non-UserPlayer members individually. */
    void sendToListeners(Message message) {
        // TODO: if we're sending updates then we also assume a small set, so
        // it's ok to send directly here, but otherwise we might want to split
        // these into separate tasks
        for (ManagedReference<? extends Player> playerRef :
                 listenerSetRef.get())
        {
            playerRef.get().send(message);
        }
    }

    /** Private implementation of {@code ChannelListener} for filters. */
    private static final class GroupChannelListener
        implements ChannelListener, Serializable
    {
    	private static final long serialVersionUID = 1;
        private final ObjectWrapper<? extends GroupFilter> wrappedFilter;
        private final ManagedReference<ChannelGroup> groupRef;
        GroupChannelListener(GroupFilter filter, ChannelGroup group) {
            wrappedFilter = new ObjectWrapper<GroupFilter>(filter);
            groupRef = AppContext.getDataManager().createReference(group);
        }
        public void receivedMessage(Channel channel, ClientSession sender,
                                    ByteBuffer message)
        {
            Message allowedMessage = wrappedFilter.get().
                filterMessage(message, sender.getName());
            if (allowedMessage != null) {
                groupRef.get().send(allowedMessage);
            }
        }
    }

}
