package me.philcali.rss.atom;

import me.philcali.rss.api.ISelfDescribing;

public interface IAtomExtension {
    boolean isCapabale(String node);
    void metadata(String node, String content, ISelfDescribing<?> builder);
}
