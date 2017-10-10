package com.cognitive.newswizard.service.translator;

import java.util.List;

import com.cognitive.newswizard.api.vo.newsfeed.FeedSourceVO;
import com.cognitive.newswizard.api.vo.newsfeed.FeedSourceGroupVO;
import com.cognitive.newswizard.service.entity.FeedSourceGroupEntity;

public class FeedSourceGroupTranslator {

	public static FeedSourceGroupEntity toEntity(final FeedSourceGroupVO feedSourceGroupVO) {
		return new FeedSourceGroupEntity(feedSourceGroupVO.getId(), feedSourceGroupVO.getName(),
				feedSourceGroupVO.getDescription(), feedSourceGroupVO.getCreationDateTime(),
				feedSourceGroupVO.getRefreshPeriod());
	}

	public static FeedSourceGroupVO toValueObject(
			final FeedSourceGroupEntity feedSourceGroupEntity,
			final List<FeedSourceVO> entries) {
		return new FeedSourceGroupVO(feedSourceGroupEntity.getId(),
				feedSourceGroupEntity.getName(), feedSourceGroupEntity.getDescription(),
				feedSourceGroupEntity.getCreationDateTime(),
				feedSourceGroupEntity.getRefreshPeriod(), entries);
	}
}
