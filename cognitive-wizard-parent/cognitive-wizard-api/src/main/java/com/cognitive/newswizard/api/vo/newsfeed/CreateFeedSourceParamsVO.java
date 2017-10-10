package com.cognitive.newswizard.api.vo.newsfeed;


/**
 * Contains all parameters needed to create a new Feed Source, with parent source group id
 * @author tiago
 *
 */
public class CreateFeedSourceParamsVO {

	private String id;
	
	private String name;
	
	private String url;
	
	private String feedSourceGroupId;
	
	private String code;

	public CreateFeedSourceParamsVO() {
	}
	
	public CreateFeedSourceParamsVO(final String id, final String name, final String url, final String feedSourceGroupId, final String code) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
		this.feedSourceGroupId = feedSourceGroupId;
		this.code = code;
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

	public String getFeedSourceGroupId() {
		return feedSourceGroupId;
	}

	public void setFeedSourceGroupId(String feedSourceGroupId) {
		this.feedSourceGroupId = feedSourceGroupId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
