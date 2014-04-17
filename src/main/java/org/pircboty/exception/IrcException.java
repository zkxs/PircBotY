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
package org.pircboty.exception;

import org.apache.commons.lang3.Validate;

/**
 * A fatal IRC error.
 *
 * @since PircBot 0.9
 * @author Origionally by:
 * <a href="http://www.jibble.org/">Paul James Mutton</a> for <a
 * href="http://www.jibble.org/pircbot.php">PircBot</a>
 * <p>
 * Forked and Maintained by in <a
 * href="http://PircBotY.googlecode.com">PircBotY</a>: Leon Blakey
 * <lord.quackstar at gmail.com>
 */
public class IrcException extends Exception {

    private static final long serialVersionUID = 503932L;

    /**
     * Constructs a new IrcException.
     *
     * @param detail The error message to report.
     */
    public IrcException(Reason reason, String detail) {
        super(generateMessage(reason, detail));
        Validate.notNull(reason, "Reason cannot be null");
        Validate.notNull(detail, "Detail cannot be null");
    }

    protected static String generateMessage(Reason reason, String detail) {
        return reason + ": " + detail;
    }

    public static enum Reason {

        AlreadyConnected,
        CannotLogin,
        ReconnectBeforeConnect,
    }
}
