package com.cognitive.newswizard.service.repository.projection;

public class CountRawFeedEntryByPeriodProjection {

	private final String id;
	
	private final Long count;

	public CountRawFeedEntryByPeriodProjection(String id, Long count) {
		this.id = id;
		this.count = count;
	}

	public String getId() {
		return id;
	}

	public Long getCount() {
		return count;
	}
	
}
