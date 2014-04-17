/**
 * Copyright (C) 2010-2013
 *
 * This file is part of PircBotY.
 *
 * PircBotY is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * PircBotY is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * PircBotY. If not, see <http://www.gnu.org/licenses/>.
 */
package org.pircboty.hooks.managers;

import com.google.common.collect.ImmutableSet;
import org.pircboty.PircBotY;
import org.pircboty.hooks.Event;
import org.pircboty.hooks.Listener;

/**
 * Manages everything about Listeners: adding, removing, and dispatching events
 * to appropriate listeners.
 * <p>
 * An important job of all methods in this class is to absorb and report
 * <u>any</u>
 * exceptions or errors before they reach {@link PircBotY}. Failure to do so
 * could break many internal long running operations. It is therefor recommended
 * to catch {@link Throwable} and report with {@link PircBotY#logException(java.lang.Throwable)
 * }
 * <
 * p>
 * Performance is another important job in implementations. Events can be
 * dispatched very quickly at times (eg a /WHO on all joined channels) so lots
 * of expensive calls can hurt performance of the entire bot. Therefor important
 * methods like {@link #dispatchEvent(org.PircBotY.hooks.Event) } should be as
 * fast as possible
 * <p/>
 * @author
 */
public interface ListenerManager<B extends PircBotY> {

    /**
     * Sends event to all appropriate listeners.
     * <p>
     * <b>For implementations:</b> Please read
     * {@link ListenerManager important information} on exception handling and
     * performance.
     *
     * @param event The event to send
     */
    public void dispatchEvent(Event<B> event);

    /**
     * Adds an listener to the list of listeners for an event.
     * <p>
     * <b>For implementations:</b> Please read
     * {@link ListenerManager important information} on exception handling and
     * performance.
     *
     * @param listener The listener to add
     * @return True if the listener was successfully added, false if not
     */
    public boolean addListener(Listener<B> listener);

    /**
     * Removes the specified Listener
     * <p>
     * <b>For implementations:</b> Please read
     * {@link ListenerManager important information} on exception handling and
     * performance.
     *
     * @param listener A Listener to remove
     * @return True if the listener was removed, false if it didn't exist or
     * wasn't removed
     */
    public boolean removeListener(Listener<B> listener);

    /**
     * Checks if the specified listener exists
     *
     * @param listener The listener <i>instance</i> to look for
     * @return True if it the listener exists, false if it doesn't
     */
    public boolean listenerExists(Listener<B> listener);

    /**
     * Gets all listeners that are in this ListenerManager
     * <p>
     * <b>For implementations:</b> Please read
     * {@link ListenerManager important information} on exception handling and
     * performance.
     *
     * @return An <b>immutable copy</b> of all listeners that are in this
     * ListenerManager
     */
    public ImmutableSet<Listener<B>> getListeners();

    /**
     * Set the current id used by the ListenerManager
     * <p>
     * If you set the current id to 0, this means that the next dispatched event
     * is going to have an id of 0
     *
     * @param currentId The id to set this ListenerManager to
     */
    public void setCurrentId(long currentId);

    /**
     * Gets the current id used by the ListenerManager
     * <p>
     * The current id is the id that's going to be handed out to an event. This
     * means that this id has not been dispatched yet.
     *
     * @return The current id
     */
    public long getCurrentId();

    /**
     * Returns the current ID then increments by 1. This means that if the
     * current id is 0 and this method is called, this method returns 0 and
     * {@link #getCurrentId()} returns 1.
     *
     * @return The current id
     */
    public long incrementCurrentId();

    public void shutdown(B bot);
}
