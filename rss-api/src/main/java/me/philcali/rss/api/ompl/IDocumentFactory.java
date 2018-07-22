package me.philcali.rss.api.ompl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public interface IDocumentFactory {
    IDocument create(InputStream input) throws DocumentCreationException;

    default IDocument create(final String input) throws DocumentCreationException {
        return create(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
    }
}
