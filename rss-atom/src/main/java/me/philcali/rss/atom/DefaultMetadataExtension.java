package me.philcali.rss.atom;

import me.philcali.rss.api.ISelfDescribing;

public class DefaultMetadataExtension implements IAtomExtension {
    @Override
    public boolean isCapabale(final String node) {
        return true;
    }
    
    @Override
    public void metadata(final String node, final String content, final ISelfDescribing<?> builder) {
        builder.addMetadata(node, content);
    }
}
