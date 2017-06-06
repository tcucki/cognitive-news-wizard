package com.cognitive.newswizard.api.vo.processed;

public class RegisterProperNounVO {

	private final String properNoun;
	
	private final String suggestion;

	private final boolean ignore;

	public RegisterProperNounVO(final String properNoun, final String suggestion, final boolean ignore) {
		this.properNoun = properNoun;
		this.suggestion = suggestion;
		this.ignore = ignore;
	}

	public String getProperNoun() {
		return properNoun;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public boolean isIgnore() {
		return ignore;
	}
}
