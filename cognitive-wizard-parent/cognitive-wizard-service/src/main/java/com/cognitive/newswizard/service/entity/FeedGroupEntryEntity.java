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
	
	/**
	 * Points to parent feed source group
	 */
	private final String feedSourceGroupId;
	
	private final String code;

	public FeedGroupEntryEntity(final String id, final String name, final String url, final Long lastRead, final String feedSourceGroupId, final String code) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
		this.lastRead = lastRead;
		this.feedSourceGroupId = feedSourceGroupId;
		this.code = code;
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

	public String getFeedSourceGroupId() {
		return feedSourceGroupId;
	}
	
	public String getCode() {
		return code;
	}
}
