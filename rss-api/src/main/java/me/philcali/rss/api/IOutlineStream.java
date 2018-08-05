package me.philcali.rss.api;

import java.io.InputStream;
import java.util.function.Function;

import me.philcali.rss.api.exception.OutlineStreamException;
import me.philcali.rss.api.ompl.IOutline;

public interface IOutlineStream extends Function<String, InputStream> {
    @Override
    InputStream apply(final String xmlUrl) throws OutlineStreamException;

    default InputStream apply(final IOutline outline) throws OutlineStreamException {
        return apply(outline.getXmlUrl());
    }
}
