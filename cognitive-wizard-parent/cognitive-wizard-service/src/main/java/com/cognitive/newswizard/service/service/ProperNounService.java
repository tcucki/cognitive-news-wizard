package com.cognitive.newswizard.service.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
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
import com.cognitive.newswizard.api.vo.processed.RegisterProperNounVO;
import com.cognitive.newswizard.service.entity.FeedTextEntity;
import com.cognitive.newswizard.service.entity.IgnoreNounEntity;
import com.cognitive.newswizard.service.entity.ProperNounEntity;
import com.cognitive.newswizard.service.repository.FeedTextRepository;
import com.cognitive.newswizard.service.repository.IgnoreNounRepository;
import com.cognitive.newswizard.service.repository.ProperNounRepository;

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
	
	private final ProperNounRepository properNounRepository;
	
	private final IgnoreNounRepository ignoreNounRepository;
	
	private final Set<String> ignoreCache; // TODO implement proper cache

	/**
	 * Caches proper nouns. Key is the proper noun found on feed. Value is the actual root proper noun which
	 * is used on mining.
	 */
	private final Map<String, String> properNounCache; // TODO implement proper cache

	@Autowired
	public ProperNounService(
			@Value("${propernounservice.feedtextpagesize:500}") final int feedTextPageSize, 
			final FeedTextRepository feedTextRepository,
			final ProperNounRepository properNounRepository,
			final IgnoreNounRepository ignoreNounRepository) {
		this.feedTextPageSize = feedTextPageSize;
		this.feedTextRepository = feedTextRepository;
		this.properNounRepository = properNounRepository;
		this.ignoreNounRepository = ignoreNounRepository;
		ignoreCache = new TreeSet<>();
		properNounCache = new LinkedHashMap<>();
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
		
		if (ignoreCache.isEmpty()) {
			loadIgnoreCache();
		}
		if (properNounCache.isEmpty()) {
			loadProperNounCache();
		}
		
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
				if (!ignoreCache.contains(k) && !properNounCache.containsKey(k)) {
					final String suggestion = findSuggestion(k);
					list.add(new MissingProperNounVO(v, k, suggestion));
				}
			}
		});
		LOGGER.info("sorting by count...");
		list.sort(new MissingProperNounVO.MissingProperNounComparator());

		return list;
	}
	
	private String findSuggestion(final String properNoun) {
		final String[] nouns = properNoun.split(" ");
		for (final String noun : nouns) {
			for (final String knownProperNoun : properNounCache.values()) {
				final String[] knownNouns = knownProperNoun.split(" ");
				for (String knownNoun : knownNouns) {
					if (noun.equals(knownNoun)) {
						return knownProperNoun;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Register bunch o proper nouns, as well as ignore nouns
	 * @param batch
	 * @return String indicating status
	 */
	public String registerProperNounBatch(final List<RegisterProperNounVO> batch) {
		LOGGER.info("Registering {} proper and ignore nouns", batch.size());
		int total = 0;
		int ignoreNouns = 0;
		int properNouns = 0;
		for (RegisterProperNounVO registerProperNounVO : batch) {
			if (registerProperNounVO.isIgnore()) {
				saveAndCacheIgnoreNoun(registerProperNounVO.getProperNoun());
				ignoreNouns++;
			} else {
				if (StringUtils.isNotBlank(registerProperNounVO.getSuggestion())) {
					registerProperNoun(registerProperNounVO.getProperNoun(), registerProperNounVO.getSuggestion());
					properNouns++;
				}
			}
			total++;
		}
		final String returnText = getBatchReturnText(total, ignoreNouns, properNouns);
		LOGGER.info(returnText);
		return returnText;
	}

	private void saveAndCacheIgnoreNoun(String ignoreNoun) {
		if (!ignoreCache.contains(ignoreNoun)) {
			try {
				ignoreNounRepository.save(new IgnoreNounEntity(ignoreNoun));
			} catch (Exception e) {
				// Just warn the error as could be trying to persist an existent entity
				LOGGER.warn(e.getMessage());
			}
			ignoreCache.add(ignoreNoun);
		}
	}

	private void registerProperNoun(final String properNoun, final String rootProperNoun) {
		if (isRootProperNoun(properNoun, rootProperNoun)) {
			saveAndCacheProperNum(properNoun, rootProperNoun);
		} else {
			// creating root proper noun
			saveAndCacheProperNum(rootProperNoun, rootProperNoun);
			// creating found proper noun pointing to root
			saveAndCacheProperNum(properNoun, rootProperNoun);
		}
	}

	private boolean saveAndCacheProperNum(String properNoun, String rootProperNoun) {
		try {
			if (!properNounCache.containsKey(properNoun)) {
				properNounRepository.save(new ProperNounEntity(properNoun, rootProperNoun));
			}
			properNounCache.put(properNoun, rootProperNoun);
			return true;
		} catch (final Exception e) {
			// Just warn the error as could be trying to persist an existent entity
			LOGGER.warn(e.getMessage());
		}
		properNounCache.put(properNoun, rootProperNoun);
		return false;
	}

	/**
	 * When both parameters are equal, this is a root proper noun pointing to it self
	 * @param properNoun
	 * @param rootProperNoun
	 * @return
	 */
	private boolean isRootProperNoun(String properNoun, String rootProperNoun) {
		return properNoun.equals(rootProperNoun);
	}

	private String getBatchReturnText(final int total, final int ignoreNouns, final int properNouns) {
		return String.format("Size batch processed: %d, proper nouns added: %d, total ignore nouns added: %d", total, properNouns, ignoreNouns);
	}

	private void processFeed(final boolean lookInParagraphs, final Map<String, Integer> properNouns, final FeedTextEntity entity) {
// TODO ignore titles		extractProperNoun(properNouns, entity.getTitle());
		if (lookInParagraphs) {
			entity.getParagraphs().forEach(paragraph -> extractProperNoun(properNouns, paragraph));
		}
	}

	private void extractProperNoun(final Map<String, Integer> properNouns, String statement) {
		extractProperNouns(statement).forEach(properNoun -> properNouns.compute(properNoun, (k, v) -> v == null ? 1 : ++v));
	}

	private void loadIgnoreCache() {
		LOGGER.info("Loading ignore list");
		ignoreNounRepository.findAll().forEach(ignoreNounEntity -> ignoreCache.add(ignoreNounEntity.getId()));
	}
	
	private void loadProperNounCache() {
		LOGGER.info("Loading ignore list");
		properNounRepository.findAll().forEach(properNounEntity -> properNounCache.put(properNounEntity.getId(), properNounEntity.getRootNoun()));
	}
}
