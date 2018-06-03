package me.philcali.rss.atom;

import me.philcali.rss.api.ISelfDescribing;

public class ContentEncodedExtension implements IAtomExtension {
    @Override
    public boolean isCapabale(final String node) {
        return node.equalsIgnoreCase("content:encoded");
    }
    
    @Override
    public void metadata(final String node, final String content, final ISelfDescribing<?> builder) {
        builder.withDescription(content);
    }
}
