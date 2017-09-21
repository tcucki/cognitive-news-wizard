package com.cognitive.newswizard.api.vo.newsfeed;

public class RawFeedReportItem {

	private final String feedGroupEntryId;
	
	private final String feedGroupEntryName;
	
	private final Long count;

	public RawFeedReportItem(final String feedGroupEntryId, final String feedGroupEntryName, final Long count) {
		this.feedGroupEntryId = feedGroupEntryId;
		this.feedGroupEntryName = feedGroupEntryName;
		this.count = count;
	}

	public String getFeedGroupEntryId() {
		return feedGroupEntryId;
	}

	public String getFeedGroupEntryName() {
		return feedGroupEntryName;
	}

	public Long getCount() {
		return count;
	}
	
	
}
