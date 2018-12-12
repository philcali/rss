package me.philcali.rss.module;

import java.util.function.Function;

import javax.inject.Singleton;

import dagger.Component;
import me.philcali.rss.service.MeResource;
import me.philcali.rss.service.NewsResource;
import me.philcali.rss.service.OPMLResource;
import me.philcali.rss.service.ServiceAuthorizer;
import me.philcali.service.annotations.Resource;
import me.philcali.service.annotations.request.Authorizer;
import me.philcali.service.annotations.request.ParamFilter;
import me.philcali.service.annotations.request.TokenFilter;
import me.philcali.service.binding.auth.BearerTokenFilter;
import me.philcali.service.reflection.filter.Base64Decode;
import me.philcali.service.reflection.filter.Base64Encode;
import me.philcali.service.reflection.filter.MarshallFilter;
import me.philcali.service.reflection.filter.UnmarshallFilter;

@Component(modules = {
        DynamoModule.class,
        ConfigModule.class,
        JacksonModule.class,
        AuthenticationModule.class,
        UserModule.class,
        HttpModule.class,
        DocumentModule.class,
        S3Module.class
})
@Singleton
public interface PetroniusServiceModule {
    @Resource
    MeResource createMeResource();

    @Resource
    OPMLResource createOPMLResource();

    @Resource
    NewsResource createNewsResource();

    @Authorizer
    @TokenFilter(BearerTokenFilter.class)
    ServiceAuthorizer createAuthorizer();

    @ParamFilter({ MarshallFilter.class, Base64Encode.class })
    Function<Object, String> createTokenEncoder();

    @ParamFilter({ Base64Decode.class, UnmarshallFilter.class })
    Function<String, Object> createTokenDecoder();
}
