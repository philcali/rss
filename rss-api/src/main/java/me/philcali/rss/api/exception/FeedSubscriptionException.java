package me.philcali.rss.api.exception;

public class FeedSubscriptionException extends RuntimeException {
    private static final long serialVersionUID = 3035792175343377862L;

    public FeedSubscriptionException(final Throwable ex) {
        super(ex);
    }
}
