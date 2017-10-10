package com.cognitive.newswizard.api.vo.newsfeed;

public class RawFeedReportItem {

	private final String feedSourceId;
	
	private final String feedSourceName;
	
	private final Long count;

	public RawFeedReportItem(final String feedSourceId, final String feedSourceName, final Long count) {
		this.feedSourceId = feedSourceId;
		this.feedSourceName = feedSourceName;
		this.count = count;
	}

	public String getFeedSourceId() {
		return feedSourceId;
	}

	public String getFeedSourceName() {
		return feedSourceName;
	}

	public Long getCount() {
		return count;
	}
	
	
}
