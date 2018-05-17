package me.philcali.rss.api.ompl;

public interface IOutline extends IOutlineContainer {
    String getType();
    String getTitle();
    String getText();
    String getHtmlUrl();
    String getXmlUrl();
}
