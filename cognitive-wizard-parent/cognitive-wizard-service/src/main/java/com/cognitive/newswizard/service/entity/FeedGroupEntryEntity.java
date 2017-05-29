package com.cognitive.newswizard.service.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "feed_group_entry")
public class FeedGroupEntryEntity {

	@Id
	private final String id;
	
	private final String name;
	
	private final String url;
	
	private final Long lastRead;
	
	private final String feedGroupId;

	public FeedGroupEntryEntity(final String id, final String name, final String url, final Long lastRead, final String feedGroupId) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
		this.lastRead = lastRead;
		this.feedGroupId = feedGroupId;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public Long getLastRead() {
		return lastRead;
	}

	public String getFeedGroupId() {
		return feedGroupId;
	}
}
