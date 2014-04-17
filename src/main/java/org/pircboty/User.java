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
package org.pircboty;

import com.google.common.collect.ImmutableSortedSet;
import java.util.UUID;
import org.apache.commons.lang3.concurrent.AtomicSafeInitializer;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.pircboty.hooks.WaitForQueue;
import org.pircboty.hooks.events.WhoisEvent;
import org.pircboty.output.OutputUser;
import org.pircboty.snapshot.UserSnapshot;

/**
 * Represents a User on the server.
 *
 * @since PircBot 1.0.0
 * @author Origionally by:
 * <a href="http://www.jibble.org/">Paul James Mutton</a> for <a
 * href="http://www.jibble.org/pircbot.php">PircBot</a>
 * <p>
 * Forked and Maintained by in <a
 * href="http://PircBotY.googlecode.com">PircBotY</a>
 */
public class User implements Comparable<User> {

    private final PircBotY bot;
    private final UserChannelDao dao;
    private final UUID userId = UUID.randomUUID();
    //Output is lazily created since it might not ever be used
    private final AtomicSafeInitializer<OutputUser> output = new AtomicSafeInitializer<OutputUser>() {
        @Override
        protected OutputUser initialize() {
            return bot.getConfiguration().getBotFactory().createOutputUser(bot, User.this);
        }
    };
    /**
     * Current nick of the user.
     */
    private String nick;
    /**
     * Realname/fullname of the user. Never changes
     */
    private String realName = "";
    /**
     * Login of the user (user!login@hostmask). Never changes
     */
    private String login = "";
    /**
     * Hostmask of the user (user!login@hostmask). Never changes
     */
    private String hostmask = "";
    /**
     * User's away status
     */
    private String awayMessage = null;
    /**
     * Users IRCop status
     */
    private boolean ircop = false;
    /**
     * The server the user is on.
     */
    private String server = "";
    /**
     * Number of hops to reach the user.
     */
    private int hops = 0;

    protected User(PircBotY bot, UserChannelDao dao, String nick) {
        this.bot = bot;
        this.dao = dao;
        this.nick = nick;
    }

    /**
     * Send a line to the user.
     *
     * @return A {@link OutputUser} for this user
     */
    public OutputUser send() {
        try {
            return output.get();
        } catch (ConcurrentException ex) {
            throw new RuntimeException("Could not generate OutputChannel for " + getNick(), ex);
        }
    }

    /**
     * Query the user with WHOIS to determine if they are verified *EXPENSIVE*.
     * This is intended to be a quick utility method, if you need more specific
     * info from the Whois then its recommended to listen for or use
	 * {@link PircBotY#waitFor(java.lang.Class) }
     *
     * @return True if the user is verified
     */
    @SuppressWarnings("unchecked")
    public boolean isVerified() {
        try {
            bot.sendRaw().rawLine("WHOIS " + getNick() + " " + getNick());
            WaitForQueue waitForQueue = new WaitForQueue(bot);
            while (true) {
                WhoisEvent event = waitForQueue.waitFor(WhoisEvent.class);
                if (!event.getNick().equals(nick)) {
                    continue;
                }
                //Got our event
                waitForQueue.close();
                return event.getRegisteredAs() != null && !event.getRegisteredAs().isEmpty();
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException("Couldn't finish querying user for verified status", ex);
        }
    }

    public UserSnapshot createSnapshot() {
        return new UserSnapshot(this);
    }

    /**
     * Get all the levels this user holds in the channel.
     *
     * @param channel The channel to get the levels from
     * @return An <b>immutable copy</b> of the levels this user holds
     */
    public ImmutableSortedSet<UserLevel> getUserLevels(Channel channel) {
        return getDao().getLevels(channel, this);
    }

    /**
     * Get all channels this user is a part of.
     *
     * @return All channels this user is a part of
     */
    public ImmutableSortedSet<Channel> getChannels() {
        return getDao().getChannels(this);
    }

    /**
     * Get all channels user has Operator status in. Be careful when storing the
     * result from this method as it may be out of date by the time you use it
     * again
     *
     * @return An <i>unmodifiable</i> Set (IE snapshot) of all channels Get all
     * channels user has Operator status in
     */
    public ImmutableSortedSet<Channel> getChannelsOpIn() {
        return getDao().getChannels(this, UserLevel.OP);
    }

    /**
     * Get all channels user has Voice status in. Be careful when storing the
     * result from this method as it may be out of date by the time you use it
     * again
     *
     * @return An <i>unmodifiable</i> Set (IE snapshot) of all channels Get all
     * channels user has Voice status in
     */
    public ImmutableSortedSet<Channel> getChannelsVoiceIn() {
        return getDao().getChannels(this, UserLevel.VOICE);
    }

    /**
     * Get all channels user has Owner status in. Be careful when storing the
     * result from this method as it may be out of date by the time you use it
     * again
     *
     * @return An <i>unmodifiable</i> Set (IE snapshot) of all channels Get all
     * channels user has Owner status in
     */
    public ImmutableSortedSet<Channel> getChannelsOwnerIn() {
        return getDao().getChannels(this, UserLevel.OWNER);
    }

    /**
     * Get all channels user has Half Operator status in. Be careful when
     * storing the result from this method as it may be out of date by the time
     * you use it again
     *
     * @return An <i>unmodifiable</i> Set (IE snapshot) of all channels Get all
     * channels user has Half Operator status in
     */
    public ImmutableSortedSet<Channel> getChannelsHalfOpIn() {
        return getDao().getChannels(this, UserLevel.HALFOP);
    }

    /**
     * Get all channels user has Super Operator status in. Simply calls 
	 * {@link UserChannelDao#getUsersSuperOps(org.PircBotY.User) }
     *
     * @return An <i>unmodifiable</i> Set (IE snapshot) of all channels Get all
     * channels user has Super Operator status in
     */
    public ImmutableSortedSet<Channel> getChannelsSuperOpIn() {
        return getDao().getChannels(this, UserLevel.SUPEROP);
    }

    /**
     * Compare {@link #getNick()} with {@link String#compareToIgnoreCase(java.lang.String)
     * }. This is useful for sorting lists of User objects.
     *
     * @param other Other user to compare to
     * @return the result of calling compareToIgnoreCase user nicks.
     */
    @Override
    public int compareTo(User other) {
        return getNick().compareToIgnoreCase(other.getNick());
    }

    /**
     * The exact server that this user is joined to.
     *
     * @return The address of the server
     */
    public String getServer() {
        return server;
    }

    /**
     * The number of hops it takes to this user.
     *
     * @return the hops
     */
    public int getHops() {
        return hops;
    }

    public boolean isAway() {
        return awayMessage != null;
    }

    public PircBotY getBot() {
        return bot;
    }

    public UserChannelDao getDao() {
        return dao;
    }

    public UUID getUserId() {
        return userId;
    }

    public AtomicSafeInitializer<OutputUser> getOutput() {
        return output;
    }

    public String getNick() {
        return nick;
    }

    public String getRealName() {
        return realName;
    }

    public String getLogin() {
        return login;
    }

    public String getHostmask() {
        return hostmask;
    }

    public String getAwayMessage() {
        return awayMessage;
    }

    public boolean isIrcop() {
        return ircop;
    }

    protected void setNick(String nick) {
        this.nick = nick;
    }

    protected void setRealName(String realName) {
        this.realName = realName;
    }

    protected void setLogin(String login) {
        this.login = login;
    }

    protected void setHostmask(String hostmask) {
        this.hostmask = hostmask;
    }

    protected void setAwayMessage(String awayMessage) {
        this.awayMessage = awayMessage;
    }

    protected void setIrcop(boolean ircop) {
        this.ircop = ircop;
    }

    protected void setServer(String server) {
        this.server = server;
    }

    protected void setHops(int hops) {
        this.hops = hops;
    }
}
