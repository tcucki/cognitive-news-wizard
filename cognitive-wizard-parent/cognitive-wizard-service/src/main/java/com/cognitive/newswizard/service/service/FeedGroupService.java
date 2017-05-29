package com.cognitive.newswizard.service.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cognitive.newswizard.api.vo.newsfeed.FeedGroupVO;
import com.cognitive.newswizard.service.entity.FeedGroupEntity;
import com.cognitive.newswizard.service.entity.FeedGroupEntryEntity;
import com.cognitive.newswizard.service.repository.FeedGroupEntryRepository;
import com.cognitive.newswizard.service.repository.FeedGroupRepository;
import com.cognitive.newswizard.service.translator.FeedGroupEntryTranslator;
import com.cognitive.newswizard.service.translator.FeedGroupTranslator;

@Component
public class FeedGroupService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(FeedGroupService.class);

	private final FeedGroupRepository feedGroupRepository;
	private final FeedGroupEntryRepository feedGroupEntryRepository;

	@Autowired
	public FeedGroupService(final FeedGroupRepository feedGroupRepository, final FeedGroupEntryRepository feedGroupEntryRepository) {
		super();
		this.feedGroupRepository = feedGroupRepository;
		this.feedGroupEntryRepository = feedGroupEntryRepository;
	}

	public FeedGroupVO create(FeedGroupVO feedGroupVO) {
		if (feedGroupVO.getCreationDateTime() == null) {
			feedGroupVO.setCreationDateTime(new Date().getTime());
		}
		LOGGER.info("Creating new feed group: {}", feedGroupVO);
		final FeedGroupEntity entity = FeedGroupTranslator.toEntity(feedGroupVO);
		return FeedGroupTranslator.toValueObject(feedGroupRepository.save(entity),
				new ArrayList<>());
	}
	
	public List<FeedGroupVO> getAll() {
		final List<FeedGroupVO> feedGroups = new ArrayList<>(); 
		final List<FeedGroupEntity> feedGroupEntities = feedGroupRepository.findAll();
		feedGroupEntities.forEach(entity -> {
			final List<FeedGroupEntryEntity> entryEntities = feedGroupEntryRepository.findByFeedGroupId(entity.getId());
			feedGroups.add(FeedGroupTranslator.toValueObject(entity, FeedGroupEntryTranslator.toValueObjects(entryEntities)));
		});
		return feedGroups;
		
	}
}
