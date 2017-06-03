package com.cognitive.newswizard.service.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.cognitive.newswizard.api.vo.processed.MissingProperNounVO;
import com.cognitive.newswizard.service.entity.FeedTextEntity;
import com.cognitive.newswizard.service.repository.FeedTextRepository;

/**
 * Services regarding Proper Noun extraction and handling
 * @author tiago
 *
 */
@Component
public class ProperNounService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProperNounService.class);
	
	private static final Pattern PATTERN = Pattern.compile("(?:\\s*\\b([A-Z][A-Za-z]+)\\b)+");

	private final int feedTextPageSize;
	
	private final FeedTextRepository feedTextRepository;

	@Autowired
	public ProperNounService(
			@Value("${propernounservice.feedtextpagesize:500}") final int feedTextPageSize, 
			final FeedTextRepository feedTextRepository) {
		this.feedTextPageSize = feedTextPageSize;
		this.feedTextRepository = feedTextRepository;
	}

	public List<String> extractProperNouns(final String statement) {
		final List<String> properNouns = new ArrayList<>();
		final Matcher matcher = PATTERN.matcher(StringUtils.stripAccents(statement));
		while (matcher.find()) {
			final String properNoun = matcher.group().trim().toUpperCase();
			properNouns.add(properNoun);
		}
		return properNouns;
	}

	/**
	 * Extracts a report with all proper nouns not registered on proper_noun collection
	 * @param lookInParagraphs	whether reporting should look also in paragraphs, or just in titles
	 * @param countThreshold	proper nouns reported must count more than this threshold
	 */
	public List<MissingProperNounVO> reportAllMissingProperNouns(final boolean lookInParagraphs, final int countThreshold) {
		final Map<String, Integer> properNouns = new HashMap<>();
		
		LOGGER.info("Starting missing proper nouns reporting");
		Page<FeedTextEntity> feedTextPages = feedTextRepository.findAll(new PageRequest(0, feedTextPageSize));
		LOGGER.info("Reading {} elements on {} pages", feedTextPages.getTotalElements(), feedTextPages.getTotalPages());
		
		feedTextPages.forEach(feed -> processFeed(lookInParagraphs, properNouns, feed));
		LOGGER.info("Processing page # 0");
		while (feedTextPages.hasNext()) {
			feedTextPages = feedTextRepository.findAll(feedTextPages.nextPageable());
			LOGGER.info("Processing page # {}", feedTextPages.getNumber());
			feedTextPages.forEach(feed -> processFeed(lookInParagraphs, properNouns, feed));
		}
		
		LOGGER.info("Filtering by threshold");
		final List<MissingProperNounVO> list = new ArrayList<MissingProperNounVO>();
		properNouns.forEach((k, v) -> {
			if (v >= countThreshold) {
				// TODO verify whether it is already known, or ignore list
				list.add(new MissingProperNounVO(v, k, null)); // TODO suggestions
			}
		});
		LOGGER.info("sorting by count...");
		list.sort(new MissingProperNounComparator());

		return list;
	}

	private void processFeed(final boolean lookInParagraphs, final Map<String, Integer> properNouns, final FeedTextEntity entity) {
		extractProperNoun(properNouns, entity.getTitle());
		if (lookInParagraphs) {
			entity.getParagraphs().forEach(paragraph -> extractProperNoun(properNouns, paragraph));
		}
	}

	private void extractProperNoun(final Map<String, Integer> properNouns, String statement) {
		extractProperNouns(statement).forEach(properNoun -> properNouns.compute(properNoun, (k, v) -> v == null ? 1 : ++v));
	}
	
	private class MissingProperNounComparator implements Comparator<MissingProperNounVO> {

		@Override
		public int compare(MissingProperNounVO o1, MissingProperNounVO o2) {
			if (o1.getCount() < o2.getCount()) {
				return 1;
			} else {
				if (o1.getCount() == o2.getCount()) {
					return 0;
				}
			}
			return -1;
		}
		
	}
}
