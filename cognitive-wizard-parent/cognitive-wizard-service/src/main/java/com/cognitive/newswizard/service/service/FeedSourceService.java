package com.cognitive.newswizard.service.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.cognitive.newswizard.api.vo.newsfeed.FeedSourceVO;
import com.cognitive.newswizard.service.entity.FeedSourceEntity;
import com.cognitive.newswizard.service.repository.FeedSourceRepository;
import com.cognitive.newswizard.service.repository.FeedSourceGroupRepository;
import com.cognitive.newswizard.service.translator.FeedSourceTranslator;

@Component
public class FeedSourceService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(FeedSourceService.class);
	
	private static final FeedSourceVO EMPTY_FEED_GROUP_ENTRY = new FeedSourceVO("", "", "", 0l, "NONE");

	private final FeedSourceRepository feedSourceRepository;
	private final FeedSourceGroupRepository feedSourceGroupRepository;
	
	private final Map<String, FeedSourceVO> feedSourceSnapshot; // TODO implement actual snapshot

	@Autowired
	public FeedSourceService(FeedSourceRepository repository, final FeedSourceGroupRepository feedSourceGroupRepository) {
		this.feedSourceRepository = repository;
		this.feedSourceGroupRepository = feedSourceGroupRepository;
		feedSourceSnapshot = new HashMap<String, FeedSourceVO>();
		loadSnapshot();
	}

	public FeedSourceVO create(final FeedSourceVO feedSourceVO, final String feedSourceGroupId) {
		validateFeedSourceGroupId(feedSourceGroupId);
		LOGGER.info("Creating new feed source: {}", feedSourceVO);
		final FeedSourceEntity entity = FeedSourceTranslator.toEntity(feedSourceVO, feedSourceGroupId);
		final FeedSourceVO saved = FeedSourceTranslator.toValueObject(feedSourceRepository.save(entity));
		feedSourceSnapshot.put(saved.getId(), saved);
		return saved;
	}
	
	public Optional<FeedSourceVO> getFromSnapshot(final String id) {
		if (feedSourceSnapshot.containsKey(id)) {
			return Optional.of(feedSourceSnapshot.get(id));
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
		feedSourceRepository.findAll().forEach(entry -> feedSourceSnapshot.put(entry.getId(), FeedSourceTranslator.toValueObject(entry)));
	}
	
	public FeedSourceVO getFromSnapshotOrDefault(final String id) {
		return getFromSnapshot(id).orElse(EMPTY_FEED_GROUP_ENTRY);
	}
}
