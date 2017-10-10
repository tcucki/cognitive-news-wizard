package com.cognitive.newswizard.api.vo.newsfeed;


/**
 * A news feed entry read via RSS, before processing
 * 
 * @author tiago
 *
 */
public class RawFeedEntryVO {

	/**
	 * Entity's ID
	 */
	private String id;

	/**
	 * Rome's id (uri)
	 */
	private String feedEntryId;

	private String title;

	private String address;

	private Long publishedDateTime;

	private String content;
	
	private String feedSourceId;
	
	private byte[] compactContent;
	
	public RawFeedEntryVO() {
		super();
	}

	public RawFeedEntryVO(final String id, final String feedEntryId, final String title,
			final String address, final Long publishedDateTime,
			final String content, final String feedSourceId,
			final byte[] compactContent) {
		super();
		this.id = id;
		this.feedEntryId = feedEntryId;
		this.title = title;
		this.address = address;
		this.publishedDateTime = publishedDateTime;
		this.content = content;
		this.feedSourceId = feedSourceId;
		this.compactContent = compactContent;
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getFeedEntryId() {
		return feedEntryId;
	}


	public void setFeedEntryId(String feedEntryId) {
		this.feedEntryId = feedEntryId;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public Long getPublishedDateTime() {
		return publishedDateTime;
	}


	public void setPublishedDateTime(Long publishedDateTime) {
		this.publishedDateTime = publishedDateTime;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}

	public String getFeedSourceId() {
		return feedSourceId;
	}
	
	public byte[] getCompactContent() {
		return compactContent;
	}
	
	public void setCompactContent(byte[] compactContent) {
		this.compactContent = compactContent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((feedEntryId == null) ? 0 : feedEntryId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RawFeedEntryVO other = (RawFeedEntryVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (feedEntryId == null) {
			if (other.feedEntryId != null)
				return false;
		} else if (!feedEntryId.equals(other.feedEntryId))
			return false;
		return true;
	}

}
