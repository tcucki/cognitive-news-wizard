package com.cognitive.newswizard.service.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "feed_text")
public class FeedTextEntity {

	private final String id;
	
	private final String rawFeedEntryId;
	
	private final String title;
	
	private final List<String> subTitles;
	
	private final List<String> paragraphs;

	public FeedTextEntity(final String id, final String rawFeedEntryId, final String title,
			final List<String> subTitles, final List<String> paragraphs) {
		this.id = id;
		this.rawFeedEntryId = rawFeedEntryId;
		this.title = title;
		this.subTitles = subTitles;
		this.paragraphs = paragraphs;
	}

	public String getId() {
		return id;
	}

	public String getRawFeedEntryId() {
		return rawFeedEntryId;
	}

	public String getTitle() {
		return title;
	}

	public List<String> getSubTitles() {
		return subTitles;
	}

	public List<String> getParagraphs() {
		return paragraphs;
	}
}
