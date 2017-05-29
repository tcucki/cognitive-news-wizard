package com.cognitive.newswizard.api.vo.processed;

import java.util.ArrayList;
import java.util.List;

import com.cognitive.newswizard.api.vo.AbstractValueObject;

@SuppressWarnings("serial")
public class FeedTextVO extends AbstractValueObject {

	private String id;
	
	private String rawFeedEntryId;
	
	private String title;
	
	private final List<String> subTitles;
	
	private final List<String> paragraphs;
	
	public FeedTextVO() {
		subTitles = new ArrayList<String>();
		paragraphs = new ArrayList<String>();
	}

	public FeedTextVO(final String id, final String rawFeedEntryId, final String title) {
		this.id = id;
		this.rawFeedEntryId = rawFeedEntryId;
		this.title = title;
		subTitles = new ArrayList<String>();
		paragraphs = new ArrayList<String>();
	}

	public FeedTextVO(final String id, final String rawFeedEntryId, final String title, final List<String> subTitles, final List<String> paragraphs) {
		this.id = id;
		this.rawFeedEntryId = rawFeedEntryId;
		this.title = title;
		this.subTitles = subTitles;
		this.paragraphs = paragraphs;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRawFeedEntryId() {
		return rawFeedEntryId;
	}

	public void setRawFeedEntryId(String rawFeedEntryId) {
		this.rawFeedEntryId = rawFeedEntryId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getSubTitles() {
		return subTitles;
	}

	public List<String> getParagraphs() {
		return paragraphs;
	}
	
	public void addSubTitle(final String subTitle) {
		subTitles.add(subTitle);
	}
	
	public void addParagraph(final String paragraph) {
		paragraphs.add(paragraph);
	}
}
