package com.cognitive.newswizard.service.service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.cognitive.newswizard.api.vo.newsfeed.RawFeedEntryVO;
import com.cognitive.newswizard.api.vo.processed.FeedTextVO;
import com.cognitive.newswizard.service.entity.FeedTextEntity;
import com.cognitive.newswizard.service.entity.RawFeedEntryEntity;
import com.cognitive.newswizard.service.repository.FeedTextRepository;
import com.cognitive.newswizard.service.repository.RawFeedEntryRepository;
import com.cognitive.newswizard.service.translator.FeedTextTranslator;
import com.cognitive.newswizard.service.translator.RawFeedEntryTranslator;
import com.cognitive.newswizard.service.util.ZipUtils;

@Component
public class FeedProcessor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FeedProcessor.class);
	
	private final NumberFormat numberFormat = new DecimalFormat("#0.000");   
	
	private final FeedTextRepository feedTextRepository;
	
	private final RawFeedEntryRepository rawFeedEntryRepository;
	
	private final FeedSourceService feedSourceService;
	
	@Autowired
	public FeedProcessor(final FeedTextRepository feedTextRepository, final RawFeedEntryRepository rawFeedEntryRepository, final FeedSourceService feedSourceService) {
		this.feedTextRepository = feedTextRepository;
		this.rawFeedEntryRepository = rawFeedEntryRepository;
		this.feedSourceService = feedSourceService;
	}
	
	public void processAllFeeds() {
		final Long start = System.currentTimeMillis();
		LOGGER.info("Processing all raw feeds (it may take a while");
		final List<RawFeedEntryEntity> allRawFeedEntities = rawFeedEntryRepository.findAll();
		long counter = 0;
		LOGGER.info("Found {} raw feeds to process", allRawFeedEntities.size());
		for (final RawFeedEntryEntity rawFeedEntryEntity : allRawFeedEntities) {
			processFeed(feedSourceService.getFromSnapshotOrDefault(rawFeedEntryEntity.getFeedSourceId()).getCode(), RawFeedEntryTranslator.toValueObject(rawFeedEntryEntity));
			counter++;
			if (counter % 10 == 0) {
				LOGGER.info("{} feeds processed", counter);
			}
		}
		final Double seconds = (System.currentTimeMillis() - start) / 1000d;
		LOGGER.info("Finished processing all raw feeds ({}) in {} seconds\n---------------------------------------------------------------------", counter, numberFormat.format(seconds));
	}

	public FeedTextVO processFeed(final String feedSourceCode, final RawFeedEntryVO rawFeedEntryVO) {
		
		if (StringUtils.isEmpty(rawFeedEntryVO.getContent()) && rawFeedEntryVO.getCompactContent() == null) {
			throw new IllegalArgumentException(String.format("[{}] No content found to be processed for feed entry id '%s'",feedSourceCode, rawFeedEntryVO.getId()));
		}
		
		final FeedTextVO feedText = new FeedTextVO(null, rawFeedEntryVO.getId(), rawFeedEntryVO.getTitle());
		
		String content = StringUtils.isEmpty(rawFeedEntryVO.getContent()) ? ZipUtils.uncompress(rawFeedEntryVO.getCompactContent()) : rawFeedEntryVO.getContent();

		feedText.getSubTitles().addAll(getAllElementValuesJsoup("h2", content));
		feedText.getSubTitles().addAll(getAllElementValuesJsoup("h3", content));
		feedText.getParagraphs().addAll(getAllElementValuesJsoup("p", content));
		feedText.getParagraphs().addAll(getAllElementValues("div", content));
		
		if (feedText.getParagraphs().isEmpty()) {
			LOGGER.warn("[{}] No content found for raw feed entry id {}\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", feedSourceCode, rawFeedEntryVO.getFeedEntryId());
		}
		
		final FeedTextEntity entity = feedTextRepository.save(FeedTextTranslator.toEntity(feedText));
		return FeedTextTranslator.toValueObject(entity);
	}

	private List<String> getAllElementValuesJsoup(final String elementName, final String content) {
		final Document document = Jsoup.parse(content);
		final Elements elements = document.select(elementName);
		return elements.stream().map(element -> element.text()).collect(Collectors.toList());
	}

	private List<String> getAllElementValues(final String element, final String html) {
		final List<String> values = new ArrayList<>();
		final Pattern pattern = Pattern.compile("<" + element + "[^>]*>([^<]+)</" + element + ">");
		final Matcher matcher = pattern.matcher(html);
		while (matcher.find()) {
			// TODO evaluate whether it is better to extract matcher.group() instead of matcher.group(1)
			final String value = matcher.group(1);
			values.add(value);
		}
		return values;
	}
}
