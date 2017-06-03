package com.cognitive.newswizard.api.vo.processed;

public class MissingProperNounVO {

	private final int count;
	
	private final String properNoun;
	
	private final String suggestion;

	public MissingProperNounVO(int count, String properNoun, String suggestion) {
		super();
		this.count = count;
		this.properNoun = properNoun;
		this.suggestion = suggestion;
	}

	public int getCount() {
		return count;
	}

	public String getProperNoun() {
		return properNoun;
	}

	public String getSuggestion() {
		return suggestion;
	}
}
