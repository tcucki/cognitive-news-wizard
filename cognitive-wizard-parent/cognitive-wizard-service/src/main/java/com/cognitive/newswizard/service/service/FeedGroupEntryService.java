package com.cognitive.newswizard.service.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.cognitive.newswizard.api.vo.newsfeed.FeedGroupEntryVO;
import com.cognitive.newswizard.service.entity.FeedGroupEntryEntity;
import com.cognitive.newswizard.service.repository.FeedGroupEntryRepository;
import com.cognitive.newswizard.service.repository.FeedGroupRepository;
import com.cognitive.newswizard.service.translator.FeedGroupEntryTranslator;

@Component
public class FeedGroupEntryService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(FeedGroupEntryService.class);

	private final FeedGroupEntryRepository feedGroupEntryRepository;
	private final FeedGroupRepository feedGroupRepository;
	
	private final Map<String, FeedGroupEntryVO> feedGroupEntrySnapshot; // TODO implement actual snapshot

	@Autowired
	public FeedGroupEntryService(FeedGroupEntryRepository repository, final FeedGroupRepository feedGroupRepository) {
		this.feedGroupEntryRepository = repository;
		this.feedGroupRepository = feedGroupRepository;
		feedGroupEntrySnapshot = new HashMap<String, FeedGroupEntryVO>();
		loadSnapshot();
	}

	public FeedGroupEntryVO create(final FeedGroupEntryVO feedGroupEntryVO, final String feedGroupId) {
		validateFeedGroupId(feedGroupId);
		LOGGER.info("Creating new feed group entry: {}", feedGroupEntryVO);
		final FeedGroupEntryEntity entity = FeedGroupEntryTranslator.toEntity(feedGroupEntryVO, feedGroupId);
		final FeedGroupEntryVO saved = FeedGroupEntryTranslator.toValueObject(feedGroupEntryRepository.save(entity));
		feedGroupEntrySnapshot.put(saved.getId(), saved);
		return saved;
	}
	
	public Optional<FeedGroupEntryVO> getFromSnapshot(final String id) {
		if (feedGroupEntrySnapshot.containsKey(id)) {
			return Optional.of(feedGroupEntrySnapshot.get(id));
		}
		return Optional.empty();
	}

	private void validateFeedGroupId(final String feedGroupId) {
		if (StringUtils.isEmpty(feedGroupId)) {
			throw new IllegalArgumentException("Empty Feed Group id is not allowed");
		}
		if (!feedGroupRepository.exists(feedGroupId)) {
			throw new IllegalArgumentException(String.format("Feed Group id '%s' not found", feedGroupId));
		}
	}
	
	private void loadSnapshot() {
		feedGroupEntryRepository.findAll().forEach(entry -> feedGroupEntrySnapshot.put(entry.getId(), FeedGroupEntryTranslator.toValueObject(entry)));
	}
}
