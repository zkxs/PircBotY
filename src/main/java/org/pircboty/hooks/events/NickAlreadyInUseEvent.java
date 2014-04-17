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
 * A nick is already in use error from server. If auto nick change isn't
 * enabled, must send a nick before server disconnects us.
 *
 * @author
 */
public class NickAlreadyInUseEvent<T extends PircBotY> extends Event<T> {

    /**
     * The nick already in use.
     */
    private final String usedNick;
    /**
     * The auto changed nick, if any.
     */
    private final String autoNewNick;
    /**
     * If auto nick change is enabled;
     */
    private final boolean autoNickChange;

    /**
     * Default constructor to setup object. Timestamp is automatically set to
     * current time as reported by {@link System#currentTimeMillis() }
     *
     * @param oldNick The old nick.
     * @param newNick The new nick.
     * @param user The user that changed their nick
     */
    public NickAlreadyInUseEvent(T bot, String usedNick, String autoNewNick, boolean autoNickChange) {
        super(bot);
        this.usedNick = usedNick;
        this.autoNewNick = autoNewNick;
        this.autoNickChange = autoNickChange;
    }

    /**
     * Respond by sending a <i>NICK</i> change
     *
     * @param newNick The nick to set
     */
    @Override
    public void respond(String newNick) {
        getBot().sendIRC().changeNick(newNick);
    }

    public String getUsedNick() {
        return usedNick;
    }

    public String getAutoNewNick() {
        return autoNewNick;
    }

    public boolean isAutoNickChange() {
        return autoNickChange;
    }
}
