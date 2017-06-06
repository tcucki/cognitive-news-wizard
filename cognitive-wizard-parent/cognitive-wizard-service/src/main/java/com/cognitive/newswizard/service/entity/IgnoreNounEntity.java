package com.cognitive.newswizard.service.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ignore_noun")
public class IgnoreNounEntity {

	/**
	 * The noun to be ignored
	 */
	@Id
	private final String id;

	public IgnoreNounEntity(String id) {
		super();
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
}
