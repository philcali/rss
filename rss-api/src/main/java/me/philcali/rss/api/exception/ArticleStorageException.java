package me.philcali.rss.api.exception;

public class ArticleStorageException extends RuntimeException {
    private static final long serialVersionUID = 4779484128528246253L;

    public ArticleStorageException(final Throwable ex) {
        super(ex);
    }
}
