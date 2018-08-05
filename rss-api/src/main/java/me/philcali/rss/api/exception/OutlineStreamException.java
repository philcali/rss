package me.philcali.rss.api.exception;

public class OutlineStreamException extends RuntimeException {
    private static final long serialVersionUID = 9073187100540700845L;

    public OutlineStreamException(final Throwable ex) {
        super(ex);
    }
}
