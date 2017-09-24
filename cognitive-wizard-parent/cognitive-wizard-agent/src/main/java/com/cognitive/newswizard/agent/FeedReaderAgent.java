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
import com.cognitive.newswizard.api.vo.newsfeed.FeedGroupEntryVO;
import com.cognitive.newswizard.api.vo.newsfeed.FeedGroupVO;
import com.cognitive.newswizard.api.vo.newsfeed.RawFeedEntryVO;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

public class FeedReaderAgent {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FeedReaderAgent.class);
	
	private final FeedGroupVO feedGroup;
	
	private final RawFeedEntryClient rawFeedEntryClient;
	
	private final List<String> processedFeedEntryUris;
	
	private final NumberFormat numberFormat = new DecimalFormat("#0.000");
	
	private final RestTemplate restTemplate;

	public FeedReaderAgent(final FeedGroupVO feedGroup, final RawFeedEntryClient rawFeedEntryClient) {
		this.feedGroup = feedGroup;
		this.rawFeedEntryClient = rawFeedEntryClient;
		processedFeedEntryUris = new ArrayList<String>();
		restTemplate = new RestTemplate();
	}

	public FeedGroupVO getFeedGroup() {
		return feedGroup;
	}
	
	public void execute() {
		final Long start = System.currentTimeMillis();
		LOGGER.info("Executing feed read agent for group [{} - '{}']", feedGroup.getId(), feedGroup.getName());
		feedGroup.getEntries().forEach(feed -> processFeed(feed));
		final Double seconds = (System.currentTimeMillis() - start) / 1000d;
		LOGGER.info("All feeds processed for group [{} - '{}'] in {} secods\n************************************************************************************", feedGroup.getId(), feedGroup.getName(), numberFormat.format(seconds));
	}

	private void processFeed(final FeedGroupEntryVO feedVO) {
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

	private void processFeedEntry(final SyndEntry feedEntry, final String feedGroupEntryId, final String feedGroupEntryName, final String feedGroupEntryCode) {
		if (processedFeedEntryUris.contains(feedEntry.getUri())) {
			return;
		};
		// TODO - verify whether feedEntry has content, so it doesn't need to be re-downloaded (like Brasil - Exame)
		LOGGER.info("[{}] Processing feed entry {} - {} '{}'", feedGroupEntryCode, feedEntry.getPublishedDate(), feedEntry.getTitle(), feedEntry.getLink());
		String feedEntryContent;
		try {
			feedEntryContent = readFeedEntryContent(feedEntry.getLink(), feedGroupEntryCode);
			if (StringUtils.isEmpty(feedEntryContent)) {
				LOGGER.warn("[{}] Empty content for feed {}\n{}", feedGroupEntryCode, feedEntry.getTitle(), feedEntry.getLink());
			}
		} catch (Exception e) {
			LOGGER.error("[{}] Exception while reading feed content [{} - {} | {} - {}]", feedGroupEntryCode, feedGroup.getId(), feedGroup.getName(), feedGroupEntryId, feedGroupEntryName, e);
			return;
		}
		final RawFeedEntryVO rawFeedEntryVO = new RawFeedEntryVO(
				null, feedEntry.getUri(), feedEntry.getTitle(), feedEntry.getLink(), 
				feedEntry.getPublishedDate() == null ? null : feedEntry.getPublishedDate().getTime(), 
				feedEntryContent, feedGroupEntryId, null);
		try {
			if (rawFeedEntryClient.create(rawFeedEntryVO) != null) {
				processedFeedEntryUris.add(feedEntry.getUri());
			}
		} catch (HttpServerErrorException e) {
			LOGGER.error("[{}] Exception on processing/persisting feed entry [{} - {} | {} - {}]", feedGroupEntryCode, feedGroup.getId(), feedGroup.getName(), feedGroupEntryId, feedGroupEntryName, e);
		}
	}
	
	private String readFeedEntryContent(final String link, final String feedGroupEntryCode) {
		
		LOGGER.info("[{}] Reading content from {}", feedGroupEntryCode, link);

		return readFeedEntryContentInternal(link, feedGroupEntryCode, 0);
	}

	private String readFeedEntryContentInternal(final String link, final String feedGroupEntryCode, final int count) {
		final ResponseEntity<String> response = restTemplate.exchange (link, HttpMethod.GET, null, String.class);

		final HttpHeaders headers = response.getHeaders();
		if (response.getStatusCode().equals(HttpStatus.MOVED_PERMANENTLY) && headers.getLocation() != null) {
			final String newUrl = headers.getLocation().toASCIIString();
			LOGGER.info("[{}] Redirecting\nfrom:\t{}\nto:\t{}\ncount:\t{}", feedGroupEntryCode, link, newUrl, count);
			return readFeedEntryContentInternal(newUrl, feedGroupEntryCode, count + 1);
		}
		return response.getBody();
	}
	
}
