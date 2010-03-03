
package org.glieseframework.message.common.impl;

import org.glieseframework.core.Entity;

import org.glieseframework.message.Message;

import java.io.Serializable;

import java.nio.ByteBuffer;


public abstract class AbstractMessage implements Message, Serializable {

    private static final int HEADER_LEN_WITH_SENDER = 7;
    private static final int HEADER_LEN_NO_SENDER = 3;

    private final short messageId;

    private final Entity sender;

    protected AbstractMessage(short messageId, Entity sender) {
        this.messageId = messageId;
        this.sender = sender;
    }

    public short getMessageId() {
        return messageId;
    }

    public Entity getSender() {
        return sender;
    }

    protected ByteBuffer setupBuffer(short payloadLength) {
    	ByteBuffer buffer =
            ByteBuffer.allocate((sender == null ? HEADER_LEN_NO_SENDER :
                                 HEADER_LEN_WITH_SENDER) + payloadLength);
        buffer.putShort(getMessageId());
        if (sender != null) {
        	buffer.put((byte) 1);
            // put the int identifier for this entity
        } else {
        	buffer.put((byte) 0);
        }
        return buffer;
    }

    public static Entity parseSender(ByteBuffer buffer) {
        byte containsSender = buffer.get();
        if (containsSender == 0) {
            return null;
        }
        int id = buffer.getInt();
        // generic way to lookup the Entity by id
        return null;
    }

}
