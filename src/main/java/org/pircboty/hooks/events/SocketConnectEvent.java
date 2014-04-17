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
package org.pircboty.hooks.events;

import org.pircboty.PircBotY;
import org.pircboty.hooks.Event;

/**
 * This event is dispatched once we successfully connected to the IRC server.
 *
 * @author
 */
public class SocketConnectEvent<T extends PircBotY> extends Event<T> {

    /**
     * Default constructor to setup object. Timestamp is automatically set to
     * current time as reported by {@link System#currentTimeMillis() }
     */
    public SocketConnectEvent(T bot) {
        super(bot);
    }

    /**
     * Responds by sending a <b>raw line</b> to the server.
     *
     * @param response The response to send
     */
    @Override
    public void respond(String response) {
        getBot().sendRaw().rawLine(response);
    }
}
