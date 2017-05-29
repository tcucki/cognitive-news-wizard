package com.cognitive.newswizard.service.translator;

import java.util.List;

import com.cognitive.newswizard.api.vo.newsfeed.FeedGroupEntryVO;
import com.cognitive.newswizard.api.vo.newsfeed.FeedGroupVO;
import com.cognitive.newswizard.service.entity.FeedGroupEntity;

public class FeedGroupTranslator {

	public static FeedGroupEntity toEntity(final FeedGroupVO feedGroupVO) {
		return new FeedGroupEntity(feedGroupVO.getId(), feedGroupVO.getName(),
				feedGroupVO.getDescription(), feedGroupVO.getCreationDateTime(),
				feedGroupVO.getRefreshPeriod());
	}

	public static FeedGroupVO toValueObject(
			final FeedGroupEntity feedGroupEntity,
			final List<FeedGroupEntryVO> entries) {
		return new FeedGroupVO(feedGroupEntity.getId(),
				feedGroupEntity.getName(), feedGroupEntity.getDescription(),
				feedGroupEntity.getCreationDateTime(),
				feedGroupEntity.getRefreshPeriod(), entries);
	}
}
