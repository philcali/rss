package me.philcali.rss.module;

import javax.inject.Singleton;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;

import dagger.Module;
import dagger.Provides;
import me.philcali.oauth.api.IClientConfigRepository;
import me.philcali.oauth.api.INonceRepository;
import me.philcali.oauth.api.ITokenRepository;
import me.philcali.oauth.dynamo.ClientConfigRepositoryDynamo;
import me.philcali.oauth.dynamo.NonceRepositoryDynamo;
import me.philcali.oauth.dynamo.TokenRepositoryDynamo;
import me.philcali.oauth.spi.config.IConfigProvider;
import me.philcali.oauth.ssm.SystemManagerConfigProvider;

@Module
public class AuthenticationModule {
    public static final String APPLICATION_NAME = "SmartRSS";
    public static final String NONCE_TABLE = "Auth.Nonces";
    public static final String TOKEN_TABLE = "Auth.Tokens";
    public static final String CONFIG_TABLE = "Auth.Clients";

    @Provides
    @Singleton
    static INonceRepository providesNonceRepository(final DynamoDB dynamoDb) {
        return new NonceRepositoryDynamo(dynamoDb.getTable(NONCE_TABLE));
    }

    @Provides
    @Singleton
    static ITokenRepository providesTokenRepository(final DynamoDB dynamoDb) {
        return new TokenRepositoryDynamo(dynamoDb.getTable(TOKEN_TABLE));
    }

    @Provides
    @Singleton
    static IClientConfigRepository providesClientConfigRepository(final DynamoDB dynamoDb) {
        return new ClientConfigRepositoryDynamo(dynamoDb.getTable(CONFIG_TABLE));
    }

    @Provides
    @Singleton
    static IConfigProvider providesAuthConfigProvider(final AWSSimpleSystemsManagement ssm) {
        return new SystemManagerConfigProvider(APPLICATION_NAME, ssm);
    }
}
