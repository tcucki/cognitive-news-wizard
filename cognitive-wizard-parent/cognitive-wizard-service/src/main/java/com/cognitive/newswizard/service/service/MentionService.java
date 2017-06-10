package com.cognitive.newswizard.service.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.cognitive.newswizard.service.entity.FeedTextEntity;
import com.cognitive.newswizard.service.entity.MentionEntity;
import com.cognitive.newswizard.service.entity.ProperNounEntity;
import com.cognitive.newswizard.service.entity.RawFeedEntryEntity;
import com.cognitive.newswizard.service.repository.FeedTextRepository;
import com.cognitive.newswizard.service.repository.MentionRepository;
import com.cognitive.newswizard.service.repository.ProperNounRepository;
import com.cognitive.newswizard.service.repository.RawFeedEntryRepository;

@Component
public class MentionService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(MentionService.class);

	private final MentionRepository mentionRepository;
	
	private final ProperNounRepository properNounRepository;
	
	private final FeedTextRepository feedTextRepository;
	
	private final ProperNounService properNounService;
	
	private final RawFeedEntryRepository rawFeedEntryRepository;

	private final int feedTextPageSize;

	public MentionService(
			@Value("${propernounservice.feedtextpagesize:500}") final int feedTextPageSize, 
			final MentionRepository mentionRepository, 
			final ProperNounRepository properNounRepository,
			final FeedTextRepository feedTextRepository,
			final RawFeedEntryRepository rawFeedEntryRepository,
			final ProperNounService properNounService) {
		this.feedTextPageSize = feedTextPageSize;
		this.mentionRepository = mentionRepository;
		this.properNounRepository = properNounRepository;
		this.feedTextRepository = feedTextRepository;
		this.rawFeedEntryRepository = rawFeedEntryRepository;
		this.properNounService = properNounService;
	}
	
	public String mineAllFeedsExtractMentioning() {
		LOGGER.info("Extracting mentions from all feeds");
		clearMentions();
		
		final List<MentionEntity> mentionEntities = processFeedPages();
		
		LOGGER.info("Finished mining feeds extracting mentioning: " + mentionEntities.size());
		return "Finished mining feeds extracting mentioning: " + mentionEntities.size();
	}

	private List<MentionEntity> processFeedPages() {
		LOGGER.info("Processing feed batches");
		
		final List<MentionEntity> mentions = new ArrayList<MentionEntity>(feedTextPageSize);
		final Map<String, String> properNounsBuffer = new HashMap<>();

		Page<FeedTextEntity> feedTextPages = feedTextRepository.findAll(new PageRequest(0, feedTextPageSize));
		LOGGER.info("Reading {} elements on {} pages", feedTextPages.getTotalElements(), feedTextPages.getTotalPages());
		
		mentions.addAll(processPage(feedTextPages, properNounsBuffer));
		while (feedTextPages.hasNext()) {
			feedTextPages = feedTextRepository.findAll(feedTextPages.nextPageable());
			mentions.addAll(processPage(feedTextPages, properNounsBuffer));
		}
		return mentions;
	}

	private List<MentionEntity> processPage(final Page<FeedTextEntity> feedTextPages, final Map<String, String> properNounsBuffer) {
		final List<MentionEntity> mentions = new ArrayList<>(feedTextPageSize);
		LOGGER.info("Processing page # {}", feedTextPages.getNumber());
		feedTextPages.forEach(feed -> {
			final MentionEntity mentionEntity = processFeed(properNounsBuffer, feed);
			if (mentionEntity != null) {
				mentions.add(mentionEntity);
			}
		});
		return mentions;
	}

	private MentionEntity processFeed(final Map<String, String> properNounsBuffer, final FeedTextEntity feed) {
		
		final Set<String> feedProperNouns = new TreeSet<>();
		final List<String> candidates = properNounService.extractProperNouns(feed.getTitle());
		
		candidates.forEach(candidate -> {
			addProperNounIfFound(properNounsBuffer, feedProperNouns, candidate);
		});
		
		feed.getParagraphs().forEach(paragraph -> {
			final List<String> paragraphCandidates = properNounService.extractProperNouns(feed.getTitle());
			paragraphCandidates.forEach(candidate -> {
				addProperNounIfFound(properNounsBuffer, feedProperNouns, candidate);
			});
		});
		
		//TODO find getPublishedDateTime()
		if (!feedProperNouns.isEmpty()) {
			return mentionRepository.save(new MentionEntity(null, feed.getId(), getPublishedDateTime(feed.getRawFeedEntryId()), feedProperNouns.toArray(new String[]{})));
		}
		return null;
	}

	
	private Long getPublishedDateTime(String rawFeedEntryId) {
		final RawFeedEntryEntity entity = rawFeedEntryRepository.findPublishedDateTimeById(rawFeedEntryId);
		if (entity != null) {
			return entity.getPublishedDateTime();
		}
		return null;
	}

	private void addProperNounIfFound(
			final Map<String, String> properNounsBuffer, final Set<String> feedProperNouns, String candidate) {
		if (feedProperNouns.contains(candidate)) {
			return;
		}
		final String rootProperNoun = findRootProperNoun(candidate, properNounsBuffer);
		if (rootProperNoun != null) {
			feedProperNouns.add(rootProperNoun);
		}
	}

	private String findRootProperNoun(final String candidate, final Map<String, String> properNounsBuffer) {
		if (properNounsBuffer.containsKey(candidate)) {
			return properNounsBuffer.get(candidate);
		}
		final ProperNounEntity properNounEntity = properNounRepository.findOne(candidate);
		if (properNounEntity != null) {
			properNounsBuffer.put(properNounEntity.getId(), properNounEntity.getRootNoun());
			return properNounEntity.getRootNoun();
		}
		return null;
	}

	private void clearMentions() {
		LOGGER.info("Cleaning all extracted mentions");
		mentionRepository.deleteAll();
	}
}
