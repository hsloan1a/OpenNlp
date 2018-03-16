package com.mybuild.model;

public class NLPCatagory {

    private final long id;
    private final String content;
    private final String sentiment;

    public NLPCatagory(long id, String content, String sentiment) {
        this.id = id;
        this.content = content;
        this.sentiment = sentiment;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

	public String getSentiment() {
		return sentiment;
	}
}
