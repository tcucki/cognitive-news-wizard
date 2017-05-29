package com.cognitive.newswizard.service.translator;

import com.cognitive.newswizard.api.vo.processed.FeedTextVO;
import com.cognitive.newswizard.service.entity.FeedTextEntity;

public class FeedTextTranslator {

	public static FeedTextEntity toEntity(final FeedTextVO valueObject) {
		return new FeedTextEntity(
				valueObject.getId(), 
				valueObject.getRawFeedEntryId(), 
				valueObject.getTitle(), 
				valueObject.getSubTitles(), 
				valueObject.getParagraphs());
	}

	public static FeedTextVO toValueObject(final FeedTextEntity entity) {
		return new FeedTextVO(
				entity.getId(), 
				entity.getRawFeedEntryId(), 
				entity.getTitle(), 
				entity.getSubTitles(), 
				entity.getParagraphs());
	}
}
