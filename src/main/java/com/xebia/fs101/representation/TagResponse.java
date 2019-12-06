package com.xebia.fs101.representation;


public class TagResponse {

    private String tag;
    private int occurrence;

    public TagResponse(String tag, int occurrence) {
        this.tag = tag;
        this.occurrence = occurrence;
    }

    public String getTag() {
        return tag;
    }

    public int getOccurrence() {
        return occurrence;
    }
}
