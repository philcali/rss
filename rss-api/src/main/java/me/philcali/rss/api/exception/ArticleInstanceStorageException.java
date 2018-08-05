package me.philcali.rss.api.exception;

public class ArticleInstanceStorageException extends RuntimeException {
    private static final long serialVersionUID = 1829781894209798729L;

    public ArticleInstanceStorageException(final Throwable ex) {
        super(ex);
    }
}
