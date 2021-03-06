package net.ae97.pircboty;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import java.util.Map;
import net.ae97.pircboty.snapshot.ChannelSnapshot;
import net.ae97.pircboty.snapshot.UserChannelMapSnapshot;
import net.ae97.pircboty.snapshot.UserSnapshot;

public class UserChannelMap<U extends User, C extends Channel> {

    protected final Multimap<U, C> userToChannelMap;
    protected final Multimap<C, U> channelToUserMap;

    public UserChannelMap() {
        channelToUserMap = MultimapBuilder.hashKeys().linkedListValues().build();
        userToChannelMap = MultimapBuilder.hashKeys().linkedListValues().build();
    }

    public UserChannelMap(Multimap<U, C> userToChannelMap, Multimap<C, U> channelToUserMap) {
        this.userToChannelMap = userToChannelMap;
        this.channelToUserMap = channelToUserMap;
    }

    protected void addUserToChannel(U user, C channel) {
        userToChannelMap.put(user, channel);
        channelToUserMap.put(channel, user);
    }

    protected void removeUserFromChannel(U user, C channel) {
        userToChannelMap.remove(user, channel);
        channelToUserMap.remove(channel, user);
    }

    protected void removeUser(U user) {
        userToChannelMap.removeAll(user).stream().forEach((curChannel) -> {
            channelToUserMap.remove(curChannel, user);
        });
    }

    protected void removeChannel(C channel) {
        channelToUserMap.removeAll(channel).stream().forEach((curUser) -> {
            userToChannelMap.remove(curUser, channel);
        });
    }

    public ImmutableSet<U> getUsers(C channel) {
        return new ImmutableSet.Builder<U>().addAll(channelToUserMap.get(channel)).build();
    }

    public ImmutableSet<C> getChannels(U user) {
        return new ImmutableSet.Builder<C>().addAll(userToChannelMap.get(user)).build();
    }

    public boolean containsEntry(U user, C channel) {
        boolean channelToUserContains = channelToUserMap.containsEntry(channel, user);
        boolean userToChannelContains = userToChannelMap.containsEntry(user, channel);
        if (channelToUserContains != userToChannelContains) {
            throw new RuntimeException("Map inconsistent! User: " + user + " | Channel: " + channel + " | channelToUserMap: " + channelToUserContains + " | userToChannelMap: " + userToChannelContains);
        }
        return channelToUserContains;
    }

    public boolean containsUser(User user) {
        boolean channelToUserContains = channelToUserMap.containsValue(user);
        boolean userToChannelContains = userToChannelMap.containsKey(user);
        if (channelToUserContains != userToChannelContains) {
            throw new RuntimeException("Map inconsistent! User: " + user + " | channelToUserMap: " + channelToUserContains + " | userToChannelMap: " + userToChannelContains);
        }
        return channelToUserContains;
    }

    protected void clear() {
        userToChannelMap.clear();
        channelToUserMap.clear();
    }

    public UserChannelMapSnapshot createSnapshot(Map<User, UserSnapshot> userSnapshots, Map<Channel, ChannelSnapshot> channelSnapshots) {
        Multimap<UserSnapshot, ChannelSnapshot> userToChannelSnapshotBuilder = MultimapBuilder.hashKeys().arrayListValues().build();
        userToChannelMap.entries().stream().forEach((curEntry) -> {
            userToChannelSnapshotBuilder.put(userSnapshots.get(curEntry.getKey()), channelSnapshots.get(curEntry.getValue()));
        });
        Multimap<ChannelSnapshot, UserSnapshot> channelToUserSnapshotBuilder = MultimapBuilder.hashKeys().arrayListValues().build();
        channelToUserMap.entries().stream().forEach((curEntry) -> {
            channelToUserSnapshotBuilder.put(channelSnapshots.get(curEntry.getKey()), userSnapshots.get(curEntry.getValue()));
        });
        return new UserChannelMapSnapshot(userToChannelSnapshotBuilder, channelToUserSnapshotBuilder);
    }
}
