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
package org.pircboty.dcc;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import org.pircboty.User;

/**
 * A DCC Chat that was initiated by another user.
 *
 * @author
 */
public class ReceiveChat extends Chat {

    public ReceiveChat(User user, Socket socket, Charset encoding) throws IOException {
        super(user, socket, encoding);
    }
}
