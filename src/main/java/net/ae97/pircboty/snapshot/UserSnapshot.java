package net.ae97.pircboty.snapshot;

import java.util.Set;
import net.ae97.pircboty.Channel;
import net.ae97.pircboty.PircBotY;
import net.ae97.pircboty.User;
import net.ae97.pircboty.UserChannelDao;
import net.ae97.pircboty.UserLevel;
import net.ae97.pircboty.Utils;

public class UserSnapshot extends User {

    private final User generatedFrom;
    private UserChannelDaoSnapshot<? extends PircBotY> dao;

    public UserSnapshot(User user) {
        super(user.getBot(), null, user.getNick());
        generatedFrom = user;
        super.setAwayMessage(user.getAwayMessage());
        super.setHops(user.getHops());
        super.setHostmask(user.getHostmask());
        super.setIrcop(user.isIrcop());
        super.setLogin(user.getLogin());
        super.setRealName(user.getRealName());
        super.setServer(user.getServer());
    }

    public UserChannelDaoSnapshot<? extends PircBotY> getSnapshotDao() {
        return dao;
    }

    @Override
    public UserChannelDao<PircBotY, User, Channel> getDao() {
        throw new UnsupportedOperationException("Cannot get Dao for a snapshot user");
    }

    @Override
    public Set<UserLevel> getUserLevels(Channel c) {
        return getSnapshotDao().getLevels(c instanceof ChannelSnapshot ? (ChannelSnapshot) c : c.createSnapshot(), this);
    }

    @Override
    public Set<Channel> getChannels() {
        return Utils.<Channel>castSet(getSnapshotDao().getChannels(this), Channel.class);
    }

    @Override
    public Set<Channel> getChannelsOpIn() {
        return Utils.<Channel>castSet(getSnapshotDao().getChannels(this, UserLevel.OP), Channel.class);
    }

    @Override
    public Set<Channel> getChannelsVoiceIn() {
        return Utils.<Channel>castSet(getSnapshotDao().getChannels(this, UserLevel.VOICE), Channel.class);
    }

    @Override
    public Set<Channel> getChannelsOwnerIn() {
        return Utils.<Channel>castSet(getSnapshotDao().getChannels(this, UserLevel.OWNER), Channel.class);
    }

    @Override
    public Set<Channel> getChannelsHalfOpIn() {
        return Utils.<Channel>castSet(getSnapshotDao().getChannels(this, UserLevel.HALFOP), Channel.class);
    }

    @Override
    public Set<Channel> getChannelsSuperOpIn() {
        return Utils.<Channel>castSet(getSnapshotDao().getChannels(this, UserLevel.SUPEROP), Channel.class);
    }

    @Override
    public int compareTo(User other) {
        return getNick().compareToIgnoreCase(other.getNick());
    }

    @Override
    public UserSnapshot createSnapshot() {
        throw new UnsupportedOperationException("Attempting to generate user snapshot from a snapshot");
    }

    @Override
    protected void setAwayMessage(String away) {
        throw new UnsupportedOperationException("Attempting to set field on user snapshot");
    }

    @Override
    protected void setHops(int hops) {
        throw new UnsupportedOperationException("Attempting to set field on user snapshot");
    }

    @Override
    protected void setHostmask(String hostmask) {
        throw new UnsupportedOperationException("Attempting to set field on user snapshot");
    }

    @Override
    protected void setIrcop(boolean ircop) {
        throw new UnsupportedOperationException("Attempting to set field on user snapshot");
    }

    @Override
    protected void setLogin(String login) {
        throw new UnsupportedOperationException("Attempting to set field on user snapshot");
    }

    @Override
    protected void setNick(String nick) {
        throw new UnsupportedOperationException("Attempting to set field on user snapshot");
    }

    @Override
    protected void setRealName(String realName) {
        throw new UnsupportedOperationException("Attempting to set field on user snapshot");
    }

    @Override
    protected void setServer(String server) {
        throw new UnsupportedOperationException("Attempting to set field on user snapshot");
    }

    public User getGeneratedFrom() {
        return generatedFrom;
    }

    public void setDao(UserChannelDaoSnapshot<? extends PircBotY> dao) {
        this.dao = dao;
    }
}
