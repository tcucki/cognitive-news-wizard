package com.cognitive.newswizard.service.translator;

import java.util.ArrayList;
import java.util.List;

import com.cognitive.newswizard.api.vo.newsfeed.FeedSourceVO;
import com.cognitive.newswizard.service.entity.FeedSourceEntity;

public class FeedSourceTranslator {

	public static FeedSourceEntity toEntity(final FeedSourceVO vo, final String feedSourceGroupId) {
		return new FeedSourceEntity(vo.getId(), vo.getName(), vo.getUrl(), vo.getLastRead(), feedSourceGroupId, vo.getCode());
	}
	
	public static FeedSourceVO toValueObject(final FeedSourceEntity entity) {
		return new FeedSourceVO(entity.getId(), entity.getName(), entity.getUrl(), entity.getLastRead(), entity.getCode());
	}
	
	public static List<FeedSourceVO> toValueObjects(final List<FeedSourceEntity> entities) {
		final List<FeedSourceVO> valueObjects = new ArrayList<FeedSourceVO>();
		entities.forEach(entity -> valueObjects.add(toValueObject(entity)));
		return valueObjects;
	}
}
