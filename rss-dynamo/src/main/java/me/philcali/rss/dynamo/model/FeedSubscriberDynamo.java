package me.philcali.rss.dynamo.model;

import com.amazonaws.services.dynamodbv2.document.Item;

import me.philcali.rss.api.IFeedSubscriber;

public class FeedSubscriberDynamo implements IFeedSubscriber {
    private final Item item;

    public FeedSubscriberDynamo(final Item item) {
        this.item = item;
    }

    @Override
    public String getUserId() {
        return item.getString("userId");
    }

    @Override
    public String getFeedId() {
        return item.getString("feedId");
    }
}
