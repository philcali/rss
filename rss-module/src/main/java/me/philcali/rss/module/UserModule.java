package me.philcali.rss.module;

import javax.inject.Singleton;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;

import dagger.Module;
import dagger.Provides;
import me.philcali.rss.api.IUserSettingsRepository;
import me.philcali.rss.dynamo.UserSettingsRepositoryDynamo;

@Module
public class UserModule {
    private static final String SETTINGS = "User.Settings";

    @Provides
    @Singleton
    static IUserSettingsRepository providesUserSettingsRepo(final DynamoDB db) {
        return new UserSettingsRepositoryDynamo(db.getTable(SETTINGS));
    }
}
