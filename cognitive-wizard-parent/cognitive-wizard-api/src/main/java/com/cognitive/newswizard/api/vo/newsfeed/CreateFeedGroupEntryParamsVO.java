package com.cognitive.newswizard.api.vo.newsfeed;


/**
 * Contains all parameters needed to create a new Feed Group Entry, with parent group id
 * @author tiago
 *
 */
public class CreateFeedGroupEntryParamsVO {

	private String id;
	
	private String name;
	
	private String url;
	
	private String feedGroupId;

	public CreateFeedGroupEntryParamsVO() {
	}
	
	public CreateFeedGroupEntryParamsVO(final String id, final String name, final String url, final String feedGroupId) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
		this.feedGroupId = feedGroupId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFeedGroupId() {
		return feedGroupId;
	}

	public void setFeedGroupId(String feedGroupId) {
		this.feedGroupId = feedGroupId;
	}
}
