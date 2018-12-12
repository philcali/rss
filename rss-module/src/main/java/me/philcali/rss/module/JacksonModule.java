package me.philcali.rss.module;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import dagger.Module;
import dagger.Provides;
import me.philcali.service.marshaller.jackson.ObjectMarshallerJackson;
import me.philcali.service.reflection.IObjectMarshaller;

@Module
public class JacksonModule {
    @Provides
    @Singleton
    static ObjectMapper getObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        return mapper;
    }

    @Provides
    @Singleton
    static IObjectMarshaller getObjectMarshaller(final ObjectMapper mapper) {
        return new ObjectMarshallerJackson(mapper);
    }
}
