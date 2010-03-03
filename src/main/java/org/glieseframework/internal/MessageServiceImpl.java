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

import com.sun.sgs.kernel.ComponentRegistry;

import com.sun.sgs.service.TransactionProxy;

import org.glieseframework.message.MessageSpec;

import org.glieseframework.message.Message;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

import java.nio.ByteBuffer;

import java.util.Properties;

import java.util.concurrent.ConcurrentHashMap;


/**
 * Implementation of {@code MessageService} that provides some basic message
 * handling. Messages are created and decoded based on the available set of
 * {@code MessageHandler}s, which are loaded on startup. The handlers are
 * loaded from a file of the form [MESSAGE_ID\w+HANDLER_CLASS\w+]*.
 * <p>
 * The {@code HANDLER_FILE_PROPERTY} is used to specify a specific file to
 * load the list from. If no file is specified, the list is loaded from
 * the default {@code DEFAULT_HANDLER_FILE} file. Note that if the file
 * path is relative, then the file is resolved with the application's data
 * directory as the root.
 */
public class MessageServiceImpl implements MessageService {

    /** The propety used to specify a specific handler list file. */
    public static final String HANDLER_FILE_PROPERTY =
        MessageServiceImpl.class.getName() + ".handler.file";

    /** The default handler list file. */
    public static final String DEFAULT_HANDLER_FILE = "handler.lst";

    // map of available handlers
    private final ConcurrentHashMap<Short,MessageHandler> handlerMap =
        new ConcurrentHashMap<Short,MessageHandler>();

    /** Creates an instance of {@code MessageServiceImpl}. */
    public MessageServiceImpl(Properties props, ComponentRegistry registry,
                              TransactionProxy proxy)
    {
        String handlerFileName =
            props.getProperty(HANDLER_FILE_PROPERTY, DEFAULT_HANDLER_FILE);
        File handlerFile = new File(handlerFileName);
        if (! handlerFile.isAbsolute()) {
            String rootName = props.getProperty("com.sun.sgs.app.root");
            handlerFile = new File(rootName, handlerFileName);
        }
        if ((! handlerFile.canRead()) || (handlerFile.isDirectory())) {
            throw new IllegalStateException("Invalid handler file: " +
                                            handlerFile.getPath());
        }
        try {
            parseHandlerFile(handlerFile);
        } catch (IOException ioe) {
            throw new IllegalStateException("Failed to read handler file", ioe);
        }
    }

    /* Implement Service */

    /** {@inheritDoc} */
    public String getName() {
        return MessageServiceImpl.class.getName();
    }

    /** {@inheritDoc} */
    public void ready() {}

    /** {@inheritDoc} */
    public void shutdown() {}

    /* Implement MessageService */

    /** {@inheritDoc} */
    public Message createMessage(MessageSpec messageSpec) {
        return getHandler(messageSpec.getMessageId()).
            createMessage(messageSpec);
    }

    /** {@inheritDoc} */
    public Message decodeMessage(ByteBuffer messageBuffer) {
        short id = messageBuffer.getShort();
        return getHandler(id).decodeMessage(id, messageBuffer);
    }

    /* Private utility methods. */

    /** Parses the handler file. TODO: should this be re-callable? */
    private void parseHandlerFile(File inputFile) throws IOException {
        FileReader reader = new FileReader(inputFile);
        StreamTokenizer stok = new StreamTokenizer(reader);
        stok.eolIsSignificant(false);
        stok.wordChars('0', '9');
        stok.wordChars('.', '.');

        try {
            while (stok.nextToken() != StreamTokenizer.TT_EOF) {
                short id = 0;
                try {
                    id = Short.parseShort(stok.sval);
                } catch (NumberFormatException nfe) {
                    throw new IOException("Illegal identifier " + stok.sval +
                                          " at line " + stok.lineno());
                }
                
                if (stok.nextToken() != StreamTokenizer.TT_WORD) {
                    throw new IOException("Expected class name at line " +
                                          stok.lineno());
                }
                
                MessageHandler handler = null;
                try {
                    Class<?> handlerClass = Class.forName(stok.sval);
                    handler = (MessageHandler) handlerClass.newInstance();
                } catch (Exception e) {
                    throw new IOException("Couldn't create handler " +
                                          stok.sval + " at line " +
                                          stok.lineno(), e);
                }
                addMessageHandler(id, handler);
            }
        } finally {
            reader.close();
        }
    }

    /** Adds a single handler if the identifier isn't already handled. */
    private void addMessageHandler(short messageId, MessageHandler handler) {
        if (handlerMap.putIfAbsent(messageId, handler) != null) {
            throw new IllegalArgumentException("Message Identifier already " +
                                               "has a handler");
        }
    }

    /** Gets the handler for the id, or throw an exception. */
    private MessageHandler getHandler(short id) {
        MessageHandler handler = handlerMap.get(id);
        if (handler == null) {
            throw new UnsupportedOperationException("Message Identifier " + id +
                                                    " is not handled");
        }
        return handler;
    }

}
