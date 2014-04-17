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
package org.pircboty.snapshot;

import org.pircboty.Channel;
import org.pircboty.User;
import org.pircboty.UserChannelDao;

/**
 *
 * @author Leon
 */
public class ChannelSnapshot extends Channel {

    private UserChannelDaoSnapshot dao;
    private final Channel generatedFrom;
    private final String mode;

    public ChannelSnapshot(Channel channel, String mode) {
        super(channel.getBot(), null, channel.getName());
        this.generatedFrom = channel;
        this.mode = mode;
        //Clone
        super.setCreateTimestamp(channel.getCreateTimestamp());
        super.setTopic(channel.getTopic());
        super.setTopicSetter(channel.getTopicSetter());
        super.setTopicTimestamp(channel.getTopicTimestamp());
        super.setChannelKey(channel.getChannelKey());
        super.setChannelLimit(channel.getChannelLimit());
        super.setChannelPrivate(channel.isChannelPrivate());
        super.setInviteOnly(channel.isInviteOnly());
        super.setModerated(channel.isModerated());
        super.setNoExternalMessages(channel.isNoExternalMessages());
        super.setSecret(channel.isSecret());
        super.setTopicProtection(channel.hasTopicProtection());
    }

    @Override
    public UserChannelDao<User, Channel> getDao() {
        //Workaround for generics
        return (UserChannelDao<User, Channel>) (Object) dao;
    }

    @Override
    public ChannelSnapshot createSnapshot() {
        throw new UnsupportedOperationException("Attempting to generate channel snapshot from a snapshot");
    }

    @Override
    public void setTopic(String topic) {
        SnapshotUtils.fail();
    }

    @Override
    public void setTopicTimestamp(long topicTimestamp) {
        SnapshotUtils.fail();
    }

    @Override
    public void setCreateTimestamp(long createTimestamp) {
        SnapshotUtils.fail();
    }

    @Override
    public void setTopicSetter(String topicSetter) {
        SnapshotUtils.fail();
    }

    @Override
    public void setModerated(boolean moderated) {
        SnapshotUtils.fail();
    }

    @Override
    public void setNoExternalMessages(boolean noExternalMessages) {
        SnapshotUtils.fail();
    }

    @Override
    public void setInviteOnly(boolean inviteOnly) {
        SnapshotUtils.fail();
    }

    @Override
    public void setSecret(boolean secret) {
        SnapshotUtils.fail();
    }

    @Override
    public void setChannelPrivate(boolean channelPrivate) {
        SnapshotUtils.fail();
    }

    @Override
    public void setTopicProtection(boolean topicProtection) {
        SnapshotUtils.fail();
    }

    @Override
    public void setChannelLimit(int channelLimit) {
        SnapshotUtils.fail();
    }

    @Override
    public void setChannelKey(String channelKey) {
        SnapshotUtils.fail();
    }

    public Channel getGeneratedFrom() {
        return generatedFrom;
    }

    public void setDao(UserChannelDaoSnapshot dao) {
        this.dao = dao;
    }
}
