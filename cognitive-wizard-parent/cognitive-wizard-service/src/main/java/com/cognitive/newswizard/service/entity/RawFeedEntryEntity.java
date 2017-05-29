package com.cognitive.newswizard.service.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "raw_feed_entry")
public class RawFeedEntryEntity {

	/**
	 * Entity's ID
	 */
	@Id
	private final String id;

	/**
	 * Rome's id (uri)
	 */
	private final String feedEntryId;

	private final String title;

	private final String address;

	private final Long publishedDateTime;

	private final String content;

	private final String feedGroupEntryId;
	
	private final byte[] compactContent;

	public RawFeedEntryEntity(
			final String id, 
			final String feedEntryId, 
			final String title,
			final String address, 
			final Long publishedDateTime, 
			final String content,
			final String feedGroupEntryId, 
			final byte[] compactContent) {
		super();
		this.id = id;
		this.feedEntryId = feedEntryId;
		this.title = title;
		this.address = address;
		this.publishedDateTime = publishedDateTime;
		this.content = content;
		this.feedGroupEntryId = feedGroupEntryId;
		this.compactContent = compactContent;
	}

	public String getId() {
		return id;
	}

	public String getFeedEntryId() {
		return feedEntryId;
	}

	public String getTitle() {
		return title;
	}

	public String getAddress() {
		return address;
	}

	public Long getPublishedDateTime() {
		return publishedDateTime;
	}

	public String getContent() {
		return content;
	}

	public String getFeedGroupEntryId() {
		return feedGroupEntryId;
	}
	
	public byte[] getCompactContent() {
		return compactContent;
	}
}
