package me.philcali.rss.service.model;

import java.util.ArrayList;
import java.util.List;

public class FeedImportResponse {
    public static class FeedImportResult {
        private final String outlineId;
        private boolean error;
        private String errorMessage;

        public FeedImportResult(final String outlineId) {
            this.outlineId = outlineId;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public String getOutlineId() {
            return outlineId;
        }

        public boolean isError() {
            return error;
        }

        public void setError(final boolean error) {
            this.error = error;
        }

        public void setErrorMessage(final String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    private List<FeedImportResult> results;

    public FeedImportResponse(final List<FeedImportResult> results) {
        this.results = results;
    }

    public FeedImportResponse() {
        this(new ArrayList<>());
    }

    public FeedImportResponse addResult(final FeedImportResult result) {
        this.results.add(result);
        return this;
    }

    public List<FeedImportResult> getResults() {
        return results;
    }
}
