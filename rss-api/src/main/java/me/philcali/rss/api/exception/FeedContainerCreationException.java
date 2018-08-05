package me.philcali.rss.api.exception;

public class FeedContainerCreationException extends RuntimeException {
    private static final long serialVersionUID = -4340733149513012460L;

    public FeedContainerCreationException(final Throwable ex) {
        super(ex);
    }
}
