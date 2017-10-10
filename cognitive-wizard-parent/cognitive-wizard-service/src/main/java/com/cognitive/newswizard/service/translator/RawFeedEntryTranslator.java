package com.cognitive.newswizard.service.translator;

import com.cognitive.newswizard.api.vo.newsfeed.RawFeedEntryVO;
import com.cognitive.newswizard.service.entity.RawFeedEntryEntity;

public class RawFeedEntryTranslator {

	public static RawFeedEntryEntity toEntity(final RawFeedEntryVO valueObject) {
		return new RawFeedEntryEntity(
				valueObject.getId(), 
				valueObject.getFeedEntryId(), 
				valueObject.getTitle(), 
				valueObject.getAddress(), 
				valueObject.getPublishedDateTime(), 
				valueObject.getContent(), 
				valueObject.getFeedSourceId(),
				valueObject.getCompactContent());
	}
	
	public static RawFeedEntryVO toValueObject(final RawFeedEntryEntity entity) {
		return new RawFeedEntryVO(
				entity.getId(), 
				entity.getFeedEntryId(), 
				entity.getTitle(), 
				entity.getAddress(), 
				entity.getPublishedDateTime(), 
				entity.getContent(), 
				entity.getFeedSourceId(),
				entity.getCompactContent());
	}
}
