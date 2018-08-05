package me.philcali.rss.api;

import java.io.InputStream;

import me.philcali.rss.api.exception.FeedContainerCreationException;

@FunctionalInterface
public interface IFeedContainerFactory {
    IFeedContainer create(final InputStream stream) throws FeedContainerCreationException;
}
