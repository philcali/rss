package me.philcali.rss.dynamo;

import java.util.Optional;

import com.amazonaws.SdkBaseException;
import com.amazonaws.services.dynamodbv2.document.Table;

import me.philcali.rss.api.IUserSettings;
import me.philcali.rss.api.IUserSettingsRepository;
import me.philcali.rss.api.exception.UserSettingsStorageException;
import me.philcali.rss.dynamo.model.UserSettingsDynamo;

public class UserSettingsRepositoryDynamo implements IUserSettingsRepository {
    private final Table userTable;

    public UserSettingsRepositoryDynamo(final Table userTable) {
        this.userTable = userTable;
    }

    @Override
    public Optional<IUserSettings> get(final String email) {
        return Optional.ofNullable(userTable.getItem(UserSettingsDynamo.EMAIL, email))
                .map(UserSettingsDynamo::new);
    }

    @Override
    public void put(final IUserSettings settings) {
        try {
            userTable.putItem(UserSettingsDynamo.toItem(settings));
        } catch (SdkBaseException e) {
            throw new UserSettingsStorageException(e);
        }
    }
}
