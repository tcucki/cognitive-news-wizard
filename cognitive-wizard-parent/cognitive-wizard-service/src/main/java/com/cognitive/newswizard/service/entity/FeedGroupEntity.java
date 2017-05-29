package com.cognitive.newswizard.service.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "feed_group")
public class FeedGroupEntity {

	/**
	 * Entity's ID
	 */
	@Id
	private final String id;

	/**
	 * Group's name
	 */
	private final String name;

	/**
	 * Group's description
	 */
	private final String description;

	/**
	 * Date & time group's creation
	 */
	private final Long creationDateTime;

	/**
	 * Period (in seconds) all news must be fetched
	 */
	private final Long refreshPeriod;

	public FeedGroupEntity(final String id, final String name, final String description,
			final Long creationDateTime, final Long refreshPeriod) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.creationDateTime = creationDateTime;
		this.refreshPeriod = refreshPeriod;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Long getCreationDateTime() {
		return creationDateTime;
	}

	public Long getRefreshPeriod() {
		return refreshPeriod;
	}
}
