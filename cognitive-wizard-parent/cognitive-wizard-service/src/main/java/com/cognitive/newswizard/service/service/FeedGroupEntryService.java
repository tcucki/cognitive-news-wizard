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
import com.cognitive.newswizard.service.repository.FeedSourceGroupRepository;
import com.cognitive.newswizard.service.translator.FeedGroupEntryTranslator;

@Component
public class FeedGroupEntryService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(FeedGroupEntryService.class);
	
	private static final FeedGroupEntryVO EMPTY_FEED_GROUP_ENTRY = new FeedGroupEntryVO("", "", "", 0l, "NONE");

	private final FeedGroupEntryRepository feedGroupEntryRepository;
	private final FeedSourceGroupRepository feedSourceGroupRepository;
	
	private final Map<String, FeedGroupEntryVO> feedGroupEntrySnapshot; // TODO implement actual snapshot

	@Autowired
	public FeedGroupEntryService(FeedGroupEntryRepository repository, final FeedSourceGroupRepository feedSourceGroupRepository) {
		this.feedGroupEntryRepository = repository;
		this.feedSourceGroupRepository = feedSourceGroupRepository;
		feedGroupEntrySnapshot = new HashMap<String, FeedGroupEntryVO>();
		loadSnapshot();
	}

	public FeedGroupEntryVO create(final FeedGroupEntryVO feedGroupEntryVO, final String feedSourceGroupId) {
		validateFeedSourceGroupId(feedSourceGroupId);
		LOGGER.info("Creating new feed group entry: {}", feedGroupEntryVO);
		final FeedGroupEntryEntity entity = FeedGroupEntryTranslator.toEntity(feedGroupEntryVO, feedSourceGroupId);
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

	private void validateFeedSourceGroupId(final String feedSourceGroupId) {
		if (StringUtils.isEmpty(feedSourceGroupId)) {
			throw new IllegalArgumentException("Empty Feed Source Group id is not allowed");
		}
		if (!feedSourceGroupRepository.exists(feedSourceGroupId)) {
			throw new IllegalArgumentException(String.format("Feed Group id '%s' not found", feedSourceGroupId));
		}
	}
	
	private void loadSnapshot() {
		feedGroupEntryRepository.findAll().forEach(entry -> feedGroupEntrySnapshot.put(entry.getId(), FeedGroupEntryTranslator.toValueObject(entry)));
	}
	
	public FeedGroupEntryVO getFromSnapshotOrDefault(final String id) {
		return getFromSnapshot(id).orElse(EMPTY_FEED_GROUP_ENTRY);
	}
}
