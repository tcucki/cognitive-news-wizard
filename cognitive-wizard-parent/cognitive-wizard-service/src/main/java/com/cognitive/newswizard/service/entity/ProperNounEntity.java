package com.cognitive.newswizard.service.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "proper_noun")
public class ProperNounEntity {

	/**
	 * The proper noun itself
	 */
	@Id
	private final String id;

	/**
	 * Points to the actual proper noun
	 */
	private final String rootNoun;

	public ProperNounEntity(final String id, final String rootNoun) {
		this.id = id;
		this.rootNoun = rootNoun;
	}

	public String getId() {
		return id;
	}

	public String getRootNoun() {
		return rootNoun;
	}
}
