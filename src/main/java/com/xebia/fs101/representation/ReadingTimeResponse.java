package com.xebia.fs101.representation;

import com.xebia.fs101.model.ReadingTime;

public class ReadingTimeResponse {
    private String articleID;
    private ReadingTime readTime;

    public ReadingTimeResponse(String articleID, ReadingTime readingTime) {
        this.articleID = articleID;
        this.readTime = readingTime;
    }

    public String getArticleID() {
        return articleID;
    }

    public ReadingTime getReadTime() {
        return readTime;
    }
}
