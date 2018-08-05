package me.philcali.rss.service.model;

import java.util.Collections;
import java.util.List;

import me.philcali.rss.api.ompl.model.Outline;

public class FeedImportRequest {
    private List<Outline> outlines = Collections.emptyList();

    public void setOutlines(final List<Outline> outlines) {
        this.outlines = outlines;
    }

    public List<Outline> getOutlines() {
        return outlines;
    }
}
