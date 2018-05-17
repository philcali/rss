package me.philcali.rss.api.ompl;

public interface IOutlineCollector<T extends IOutlineCollector<T>> {
    T withOutlines(IOutline ... outlines);
}
