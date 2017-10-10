package com.cognitive.newswizard.agent;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.cognitive.newswizard.agent.client.RawFeedEntryClient;
import com.cognitive.newswizard.api.vo.newsfeed.FeedSourceVO;
import com.cognitive.newswizard.api.vo.newsfeed.FeedSourceGroupVO;
import com.cognitive.newswizard.api.vo.newsfeed.RawFeedEntryVO;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

public class FeedReaderAgent {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FeedReaderAgent.class);
	
	private final FeedSourceGroupVO feedSourceGroup;
	
	private final RawFeedEntryClient rawFeedEntryClient;
	
	private final List<String> processedFeedEntryUris;
	
	private final NumberFormat numberFormat = new DecimalFormat("#0.000");
	
	private final RestTemplate restTemplate;

	public FeedReaderAgent(final FeedSourceGroupVO feedSourceGroup, final RawFeedEntryClient rawFeedEntryClient) {
		this.feedSourceGroup = feedSourceGroup;
		this.rawFeedEntryClient = rawFeedEntryClient;
		processedFeedEntryUris = new ArrayList<String>();
		restTemplate = new RestTemplate();
	}

	public FeedSourceGroupVO getFeedSourceGroup() {
		return feedSourceGroup;
	}
	
	public void execute() {
		final Long start = System.currentTimeMillis();
		LOGGER.info("\n************************************************************************************\nExecuting feed read agent for group [{} - '{}']", feedSourceGroup.getId(), feedSourceGroup.getName());
		feedSourceGroup.getEntries().forEach(feed -> processFeed(feed));
		final Double seconds = (System.currentTimeMillis() - start) / 1000d;
		LOGGER.info("All feeds processed for group [{} - '{}'] in {} secods\n************************************************************************************", feedSourceGroup.getId(), feedSourceGroup.getName(), numberFormat.format(seconds));
	}

	private void processFeed(final FeedSourceVO feedVO) {
		final Long start = System.currentTimeMillis();
		LOGGER.info("[{}] Reading feed {} - {}", feedVO.getCode(), feedVO.getName(), feedVO.getUrl());
		try {
			final SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(feedVO.getUrl())));
			LOGGER.info("[{}] Processing {}", feedVO.getCode(), feed.getTitle());
			feed.getEntries().forEach(feedEntry -> processFeedEntry(feedEntry, feedVO.getId(), feedVO.getName(), feedVO.getCode()));
		} catch (FeedException | IOException e) {
			throw new RuntimeException("[" + feedVO.getCode() + "] Exception on read feed", e);
		}
		final Double seconds = (System.currentTimeMillis() - start) / 1000d;
		LOGGER.info("[{}] Finished processing feed {} in {} seconds - {}\n---------------------------------------------------------------------", feedVO.getCode(), feedVO.getName(), numberFormat.format(seconds), feedVO.getUrl());
	}

	private void processFeedEntry(final SyndEntry feedEntry, final String feedSourceId, final String feedSourceName, final String feedSourceCode) {
		if (processedFeedEntryUris.contains(feedEntry.getUri())) {
			return;
		};
		// TODO - verify whether feedEntry has content, so it doesn't need to be re-downloaded (like Brasil - Exame)
		final Long start = System.currentTimeMillis();
		LOGGER.info("[{}] Processing feed entry {} - {} '{}'", feedSourceCode, feedEntry.getPublishedDate(), feedEntry.getTitle(), feedEntry.getLink());
		String feedEntryContent;
		try {
			feedEntryContent = readFeedEntryContent(feedEntry.getLink(), feedSourceCode);
			if (StringUtils.isEmpty(feedEntryContent)) {
				LOGGER.warn("[{}] Empty content for feed {}\n{}", feedSourceCode, feedEntry.getTitle(), feedEntry.getLink());
			}
		} catch (Exception e) {
			LOGGER.error("[{}] Exception while reading feed content [{} - {} | {} - {}]", feedSourceCode, feedSourceGroup.getId(), feedSourceGroup.getName(), feedSourceId, feedSourceName, e);
			return;
		}
		final RawFeedEntryVO rawFeedEntryVO = new RawFeedEntryVO(
				null, feedEntry.getUri(), feedEntry.getTitle(), feedEntry.getLink(), 
				feedEntry.getPublishedDate() == null ? null : feedEntry.getPublishedDate().getTime(), 
				feedEntryContent, feedSourceId, null);
		try {
			final Long serviceStart = System.currentTimeMillis();
			LOGGER.info("[{}] Sending entry to service {} '{}'", feedSourceCode, feedEntry.getTitle(), feedEntry.getLink());
			if (rawFeedEntryClient.create(rawFeedEntryVO) != null) {
				processedFeedEntryUris.add(feedEntry.getUri());
			}
			final Double seconds = (System.currentTimeMillis() - serviceStart) / 1000d;
			LOGGER.info("[{}] Processing feed time on service {} seconds entry {} '{}'", feedSourceCode, numberFormat.format(seconds), feedEntry.getTitle(), feedEntry.getLink());
		} catch (HttpServerErrorException e) {
			LOGGER.error("[{}] Exception on processing/persisting feed entry [{} - {} | {} - {}]", feedSourceCode, feedSourceGroup.getId(), feedSourceGroup.getName(), feedSourceId, feedSourceName, e);
		}
		final Double totalSeconds = (System.currentTimeMillis() - start) / 1000d;
		LOGGER.info("[{}] Total processing feed time {} seconds entry {} '{}'", feedSourceCode, numberFormat.format(totalSeconds), feedEntry.getTitle(), feedEntry.getLink());
	}
	
	private String readFeedEntryContent(final String link, final String feedSourceCode) {
		
		final Long start = System.currentTimeMillis();
		LOGGER.info("[{}] Reading content from {}", feedSourceCode, link);

		final String content = readFeedEntryContentInternal(link, feedSourceCode, 0);

		final Double seconds = (System.currentTimeMillis() - start) / 1000d;

		LOGGER.info("[{}] Content read in {} seconds from {}", feedSourceCode, numberFormat.format(seconds), link);

		return content;
	}

	private String readFeedEntryContentInternal(final String link, final String feedSourceCode, final int count) {
		final ResponseEntity<String> response = restTemplate.exchange (link, HttpMethod.GET, null, String.class);

		final HttpHeaders headers = response.getHeaders();
		if (response.getStatusCode().equals(HttpStatus.MOVED_PERMANENTLY) && headers.getLocation() != null) {
			final String newUrl = headers.getLocation().toASCIIString();
			LOGGER.info("[{}] Redirecting\nfrom:\t{}\nto:\t{}\ncount:\t{}", feedSourceCode, link, newUrl, count);
			return readFeedEntryContentInternal(newUrl, feedSourceCode, count + 1);
		}
		return response.getBody();
	}
	
}
