package me.philcali.rss.module;

import java.io.IOException;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;

import dagger.Module;
import dagger.Provides;
import me.philcali.service.reflection.IObjectMarshaller;


@Module
public class JacksonModule {
    @Provides
    @Singleton
    static IObjectMarshaller providesObjectMarshaller(final ObjectMapper mapper) {
        // TODO: move this into a top-level class
        return new IObjectMarshaller() {

            @Override
            public <T> T unmarshall(final String content, final Class<T> objectClass) throws IOException {
                return mapper.readValue(content, objectClass);
            }

            @Override
            public String marshall(final Object obj) throws IOException {
                return mapper.writeValueAsString(obj);
            }
        };
    }

    @Provides
    @Singleton
    static ObjectMapper providesObjectMapper() {
        // Customized Jackson Marshaller
        return new ObjectMapper();
    }
}
