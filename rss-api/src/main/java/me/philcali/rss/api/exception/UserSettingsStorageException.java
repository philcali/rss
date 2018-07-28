package me.philcali.rss.api.exception;

public class UserSettingsStorageException extends RuntimeException {
    private static final long serialVersionUID = -5719436739065100658L;

    public UserSettingsStorageException(final Throwable ex) {
        super(ex);
    }
}
