package com.cognitive.newswizard.api.vo.newsfeed;

import com.cognitive.newswizard.api.vo.AbstractValueObject;

/**
 * Represents a source of RSS Feed
 * @author tiago
 *
 */
@SuppressWarnings("serial")
public class FeedSourceVO extends AbstractValueObject {

	private String id;
	
	private String name;
	
	private String url;
	
	private Long lastRead;
	
	private String code;
	
	public FeedSourceVO() {
		super();
	}

	public FeedSourceVO(final String id, final String name, final String url, final Long lastRead, final String code) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
		this.lastRead = lastRead;
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

	public Long getLastRead() {
		return lastRead;
	}

	public void setLastRead(Long lastRead) {
		this.lastRead = lastRead;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		FeedSourceVO other = (FeedSourceVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
