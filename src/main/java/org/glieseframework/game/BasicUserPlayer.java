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

package org.glieseframework.game;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.NameNotBoundException;

import org.glieseframework.core.Item;
import org.glieseframework.core.ObjectWrapper;
import org.glieseframework.core.UserPlayer;

import java.io.Serializable;

import java.util.Collection;
import java.util.Collections;


/**
 * An abstract utility extension of {@code UserPlayer} that implements
 * some common functionality. Specifically, this class manages the
 * player's current {@code GameProxy}, provides basic support for
 * mode switching on login/logout (using a "logged out" game mode so
 * that the player is always in some mode), and implements equality based
 * on the player's name. Use of this class is optional, but for many games
 * extending this class rather than extending {@code UserPlayer} directly
 * will provide all the functionality needed to develop a complete game.
 */
public abstract class BasicUserPlayer extends UserPlayer
    implements Serializable
{
    private static final long serialVersionUID = 1;

    // TODO: for now, we'll just use a no-op mode when logged out, but
    // in the future we could make this configurable to do some
    // interesting off-line behavior
    private static final String LOGGED_OUT_MODE_NAME =
        "slipstream:mode:logged_out";

    // TODO: must a GameMode always be a ManagedObject?
    private ObjectWrapper<? extends GameProxy> proxyWrapper = null;

    // the player's name
    private final String userName;

    // the player's id...this is ignired if getId() is overridden
    private final long userId;

    /**  */
    protected BasicUserPlayer(String userName) {
        this.userName = userName;
        this.userId = AppContext.getDataManager().getObjectId(this).longValue();
        // TODO: is this the right place to start? It's nice to always
        // have a non-null proxy, even before being assigned somewhere,
        // but this is really just to handle bootstrapping..or we could
        // require that all extending classes call setGameProxy() in
        // their constructors (not really clean) or pass a mode/proxy
        // to this constructor (could be hard to get the order right).
        setGameProxy(getLoggedOutMode().join(this, null));
    }

    /**  */
    protected final void setGameProxy(GameProxy gameProxy) {
        AppContext.getDataManager().markForUpdate(this);
        proxyWrapper = new ObjectWrapper<GameProxy>(gameProxy);
    }

    /* Partial implementation of Player */

    /** {@inheritDoc} */
    public final String getName() {
        return userName;
    }

    /** {@inheritDoc} */
    public long getId() {
        return userId;
    }

    /** {@inheritDoc} */
    public final GameProxy getGameProxy() {
        return proxyWrapper == null ? null : proxyWrapper.get();
    }

    /** Returns true. */
    public boolean wantsToHearMessages() {
        return true;
    }

    /** By default assume an empty inventory. */
    public Collection<? extends Item> getItems() {
        return Collections.emptySet();
    }

    /** Implement equality */

    /** {@inheritDoc} */
    public boolean equals(Object o) {
        return (o instanceof BasicUserPlayer) &&
            userName.equals(((BasicUserPlayer)o).userName);
    }

    /** {@inheritDoc} */
    public int hashCode() {
        return userName.hashCode();
    }

    /* Implement connect/disconnect handling */

    /** Gets the player into an initial game mode. */
    public final void loggedIn(Game game) {
        proxyWrapper.get().handleEvent(null/*send the left message*/);
        setGameProxy(game.join(this));
    }

    /** Releases the current game mode. */
    public final void loggedOut() {
        proxyWrapper.get().loggedOut();
        setGameProxy(getLoggedOutMode().join(this, null));
    }

    /** Utility for accessing the logged-out mode. */
    private static GameMode getLoggedOutMode() {
        try {
            return (GameMode) (AppContext.getDataManager().
                               getBinding(LOGGED_OUT_MODE_NAME));
        } catch (NameNotBoundException nnbe) {
            GameMode loggedOutMode = new LoggedOutMode();
            AppContext.getDataManager().
                setBinding(LOGGED_OUT_MODE_NAME, loggedOutMode);
            return loggedOutMode;
        }
    }

}
