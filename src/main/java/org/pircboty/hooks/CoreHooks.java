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
package org.pircboty.hooks;

import java.util.Date;
import org.pircboty.PircBotY;
import org.pircboty.hooks.events.FingerEvent;
import org.pircboty.hooks.events.PingEvent;
import org.pircboty.hooks.events.ServerPingEvent;
import org.pircboty.hooks.events.TimeEvent;
import org.pircboty.hooks.events.VersionEvent;
import org.pircboty.hooks.managers.ListenerManager;

/**
 * Core Hooks of PircBotY that preform basic and expected operations. Any
 * listener that wishes to duplicate functionality should <b>replace</b>
 * CoreHooks in the {@link ListenerManager} with a subclass of this class (this
 * way you don't have to duplicate all the functionality).
 * <p>
 * <b>Warning:</b> Removing CoreHooks without providing a replacement will
 * produce undesired results like server timeouts due to not responding to
 * pings.
 * <p/>
 * @author
 */
public class CoreHooks<B extends PircBotY> extends ListenerAdapter<B> {

    @Override
    public void onFinger(FingerEvent<B> event) {
        event.getUser().send().ctcpResponse("FINGER " + event.getBot().getConfiguration().getFinger());
    }

    @Override
    public void onPing(PingEvent<B> event) {
        event.getUser().send().ctcpResponse("PING " + event.getPingValue());
    }

    @Override
    public void onServerPing(ServerPingEvent<B> event) {
        event.getBot().sendRaw().rawLine("PONG " + event.getResponse());
    }

    @Override
    public void onTime(TimeEvent<B> event) {
        event.getUser().send().ctcpResponse("TIME " + new Date().toString());
    }

    @Override
    public void onVersion(VersionEvent<B> event) {
        event.getUser().send().ctcpResponse("VERSION " + event.getBot().getConfiguration().getVersion());
    }
}
