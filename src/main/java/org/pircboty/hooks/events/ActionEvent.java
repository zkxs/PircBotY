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

import org.pircboty.Channel;
import org.pircboty.PircBotY;
import org.pircboty.User;
import org.pircboty.hooks.Event;
import org.pircboty.hooks.types.GenericChannelUserEvent;
import org.pircboty.hooks.types.GenericMessageEvent;

/**
 * Called whenever an ACTION is sent from a user. E.g. such events generated by
 * typing "/me goes shopping" in most IRC clients.
 *
 * @author
 */
public class ActionEvent<T extends PircBotY> extends Event<T> implements GenericMessageEvent<T>, GenericChannelUserEvent<T> {

    private final User user;
    private final Channel channel;
    private final String action;

    /**
     * Default constructor to setup object. Timestamp is automatically set to
     * current time as reported by {@link System#currentTimeMillis() }
     *
     * @param user The user object representing the user that sent the message
     * @param channel The channel object representing the target channel of the
     * action. A value of <code>null</code> means that this is is a private
     * message, not a channel
     * @param action The action carried out by the user.
     */
    public ActionEvent(T bot, User user, Channel channel, String action) {
        super(bot);
        this.user = user;
        this.channel = channel;
        this.action = action;
    }

    /**
     * Returns the action sent by the user. Same result as getAction
     *
     * @return Action sent by the user
     */
    @Override
    public String getMessage() {
        return action;
    }

    /**
     * Respond to an action with an action in either the channel that the
     * message came from or a private message.
     * <p>
     * Example
     * <pre>
     *   * SomeUser thinks that this is awesome
     *   * PircBotY agrees
     * </pre>
     *
     * @param response The response to send
     */
    @Override
    public void respond(String response) {
        if (getChannel() == null) {
            getUser().send().action(response);
        } else {
            getChannel().send().action(response);
        }
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }
}
