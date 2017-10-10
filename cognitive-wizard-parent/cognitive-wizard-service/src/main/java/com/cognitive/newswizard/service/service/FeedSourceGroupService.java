package com.cognitive.newswizard.service.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cognitive.newswizard.api.vo.newsfeed.FeedSourceGroupVO;
import com.cognitive.newswizard.service.entity.FeedSourceGroupEntity;
import com.cognitive.newswizard.service.entity.FeedSourceEntity;
import com.cognitive.newswizard.service.repository.FeedSourceRepository;
import com.cognitive.newswizard.service.repository.FeedSourceGroupRepository;
import com.cognitive.newswizard.service.translator.FeedSourceTranslator;
import com.cognitive.newswizard.service.translator.FeedSourceGroupTranslator;

@Component
public class FeedSourceGroupService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(FeedSourceGroupService.class);

	private final FeedSourceGroupRepository feedSourceGroupRepository;
	private final FeedSourceRepository feedSourceRepository;

	@Autowired
	public FeedSourceGroupService(final FeedSourceGroupRepository feedSourceGroupRepository, final FeedSourceRepository feedSourceRepository) {
		super();
		this.feedSourceGroupRepository = feedSourceGroupRepository;
		this.feedSourceRepository = feedSourceRepository;
	}

	public FeedSourceGroupVO create(FeedSourceGroupVO feedSourceGroupVO) {
		if (feedSourceGroupVO.getCreationDateTime() == null) {
			feedSourceGroupVO.setCreationDateTime(new Date().getTime());
		}
		LOGGER.info("Creating new feed source group: {}", feedSourceGroupVO);
		final FeedSourceGroupEntity entity = FeedSourceGroupTranslator.toEntity(feedSourceGroupVO);
		return FeedSourceGroupTranslator.toValueObject(feedSourceGroupRepository.save(entity),
				new ArrayList<>());
	}
	
	public List<FeedSourceGroupVO> getAll() {
		final List<FeedSourceGroupVO> feedSourceGroups = new ArrayList<>(); 
		final List<FeedSourceGroupEntity> feedSourceGroupEntities = feedSourceGroupRepository.findAll();
		feedSourceGroupEntities.forEach(feedSourceGroupEntity -> {
			final List<FeedSourceEntity> entryEntities = feedSourceRepository.findByFeedSourceGroupId(feedSourceGroupEntity.getId());
			feedSourceGroups.add(FeedSourceGroupTranslator.toValueObject(feedSourceGroupEntity, FeedSourceTranslator.toValueObjects(entryEntities)));
		});
		return feedSourceGroups;
		
	}
}
