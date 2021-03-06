package net.ae97.pircboty.api.events;

import net.ae97.pircboty.Channel;
import net.ae97.pircboty.PircBotY;
import net.ae97.pircboty.User;
import net.ae97.pircboty.api.Event;
import net.ae97.pircboty.generics.GenericChannelUserEvent;

public class TopicEvent extends Event implements GenericChannelUserEvent {

    private final Channel channel;
    private final String oldTopic;
    private final String topic;
    private final User user;
    private final boolean changed;
    private final long date;

    public TopicEvent(PircBotY bot, Channel channel, String oldTopic, String topic, User user, long date, boolean changed) {
        super(bot);
        this.channel = channel;
        this.oldTopic = oldTopic;
        this.topic = topic;
        this.user = user;
        this.changed = changed;
        this.date = date;
    }

    @Override
    public void respond(String response) {
        getChannel().send().message(getUser(), response);
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public User getUser() {
        return user;
    }
}
