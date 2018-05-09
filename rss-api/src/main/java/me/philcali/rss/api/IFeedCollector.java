package me.philcali.rss.api;

public interface IFeedCollector<T extends IFeedCollector<T>> {
    IFeedCollector<T> withFeeds(IFeed ... feeds);
}
