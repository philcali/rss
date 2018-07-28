package me.philcali.rss.api;

import java.util.Optional;

import me.philcali.rss.api.exception.UserSettingsStorageException;

public interface IUserSettingsRepository {
    Optional<IUserSettings> get(String email);

    void put(IUserSettings settings) throws UserSettingsStorageException;
}
