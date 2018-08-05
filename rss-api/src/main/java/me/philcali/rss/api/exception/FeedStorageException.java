package me.philcali.rss.api.exception;

public class FeedStorageException extends RuntimeException {
    private static final long serialVersionUID = -3907030726583544494L;

    public FeedStorageException(final Throwable ex) {
        super(ex);
    }
}
