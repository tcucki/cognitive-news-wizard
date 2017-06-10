package com.cognitive.newswizard.service.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mention")
public class MentionEntity {

	@Id
	private final String id;
	
	private final String feedId;
	
	private final Long publishedDateTime;

	private final String[] properNoun;

	public MentionEntity(final String id, final String feedId, final Long publishedDateTime, final String[] properNoun) {
		this.id = id;
		this.feedId = feedId;
		this.publishedDateTime = publishedDateTime;
		this.properNoun = properNoun;
	}

	public String getId() {
		return id;
	}

	public String getFeedId() {
		return feedId;
	}

	public Long getPublishedDateTime() {
		return publishedDateTime;
	}

	public String[] getProperNoun() {
		return properNoun;
	}

}
