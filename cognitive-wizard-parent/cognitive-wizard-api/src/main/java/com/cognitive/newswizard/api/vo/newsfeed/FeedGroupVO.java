package com.cognitive.newswizard.api.vo.newsfeed;

import java.util.List;

import com.cognitive.newswizard.api.vo.AbstractValueObject;

/**
 * Identify a group of news sources which must be processed independently.
 * @author tiago
 *
 */
@SuppressWarnings("serial")
public class FeedGroupVO extends AbstractValueObject {

	/**
	 * Entity's ID
	 */
	private String id;

	/**
	 * Group's name
	 */
	private String name;

	/**
	 * Group's description
	 */
	private String description;

	/**
	 * Date & time group's creation
	 */
	private Long creationDateTime;

	/**
	 * Period (in seconds) all news must be fetched
	 */
	private Long refreshPeriod;
	
	private List<FeedGroupEntryVO> entries;
	
	public FeedGroupVO() {
	}

	public FeedGroupVO(final String id, final String name,
			final String description, final Long creationDateTime,
			final Long refreshPeriod, final List<FeedGroupEntryVO> entries) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.creationDateTime = creationDateTime;
		this.refreshPeriod = refreshPeriod;
		this.entries = entries;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(Long creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public Long getRefreshPeriod() {
		return refreshPeriod;
	}

	public void setRefreshPeriod(Long refreshPeriod) {
		this.refreshPeriod = refreshPeriod;
	}

	public List<FeedGroupEntryVO> getEntries() {
		return entries;
	}

	public void setEntries(List<FeedGroupEntryVO> entries) {
		this.entries = entries;
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
		FeedGroupVO other = (FeedGroupVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
