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

package org.glieseframework.region;

import com.sun.sgs.app.ManagedObject;


/**
 * Used by the {@code Game} and the {@code GameMode}s it creates to
 * instantiate {@code Region}s. Currently this is just a placeholder.
 * <p>
 * TODO: what does the query interface look like? How do we abstract
 * this such that it's actually more useful than directly creating
 * {@code Region} instances as needed? How do we not make it too hard
 * to describe the type of region and the associated parameters? Do all
 * implementations of {@code Region} need to take the same set of
 * parameters?
 * <p>
 * NOTE: this object could simply be transient, passed only to {@code Game}
 * at startup and then never used again. This wouldn't support games that
 * need to create new {@code Region} instances as the game progresses. For
 * this reason the factory extends {@code ManagedObject} so that if a
 * {@code Game} needs to create new {@code Region}s after initialization
 * it can keep a reference to the factory. Another approach would be to
 * have a {@code Manager} provide access, which would provide more
 * flexibility, but also makes it less clear who should use this factory
 * and to what end.
 */
public interface RegionFactory extends ManagedObject {

}
