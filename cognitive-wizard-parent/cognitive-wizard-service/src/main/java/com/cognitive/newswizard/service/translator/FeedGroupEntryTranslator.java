package com.cognitive.newswizard.service.translator;

import java.util.ArrayList;
import java.util.List;

import com.cognitive.newswizard.api.vo.newsfeed.FeedGroupEntryVO;
import com.cognitive.newswizard.service.entity.FeedGroupEntryEntity;

public class FeedGroupEntryTranslator {

	public static FeedGroupEntryEntity toEntity(final FeedGroupEntryVO vo, final String feedSourceGroupId) {
		return new FeedGroupEntryEntity(vo.getId(), vo.getName(), vo.getUrl(), vo.getLastRead(), feedSourceGroupId, vo.getCode());
	}
	
	public static FeedGroupEntryVO toValueObject(final FeedGroupEntryEntity entity) {
		return new FeedGroupEntryVO(entity.getId(), entity.getName(), entity.getUrl(), entity.getLastRead(), entity.getCode());
	}
	
	public static List<FeedGroupEntryVO> toValueObjects(final List<FeedGroupEntryEntity> entities) {
		final List<FeedGroupEntryVO> valueObjects = new ArrayList<FeedGroupEntryVO>();
		entities.forEach(entity -> valueObjects.add(toValueObject(entity)));
		return valueObjects;
	}
}
