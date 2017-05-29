package com.cognitive.newswizard.agent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public FeedReaderAgent(final FeedGroupVO feedGroup, final RawFeedEntryClient rawFeedEntryClient) {
		this.feedGroup = feedGroup;
		this.rawFeedEntryClient = rawFeedEntryClient;
		processedFeedEntryUris = new ArrayList<String>();
	}

	public FeedGroupVO getFeedGroup() {
		return feedGroup;
	}
	
	public void execute() {
		final Long start = System.currentTimeMillis();
		LOGGER.info("Executing feed read agent for group {} - '{}'", feedGroup.getId(), feedGroup.getName());
		feedGroup.getEntries().forEach(feed -> processFeed(feed));
		final Double seconds = (System.currentTimeMillis() - start) / 1000d;
		LOGGER.info("All feeds processed in {} secods\n************************************************************************************", numberFormat.format(seconds));
	}

	private void processFeed(final FeedGroupEntryVO feedVO) {
		final Long start = System.currentTimeMillis();
		LOGGER.info("Reading feed {} - {}", feedVO.getName(), feedVO.getUrl());
		try {
			final SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(feedVO.getUrl())));
			LOGGER.info("Processing {}", feed.getTitle());
			feed.getEntries().forEach(feedEntry -> processFeedEntry(feedEntry, feedVO.getId()));
		} catch (FeedException | IOException e) {
			throw new RuntimeException("Exception on read feed", e);
		}
		final Double seconds = (System.currentTimeMillis() - start) / 1000d;
		LOGGER.info("Finished processing feed {} in {} seconds - {}\n---------------------------------------------------------------------", feedVO.getName(), numberFormat.format(seconds), feedVO.getUrl());
	}

	private void processFeedEntry(final SyndEntry feedEntry, final String feedGroupEntryId) {
		if (processedFeedEntryUris.contains(feedEntry.getUri())) {
			return;
		};
		// TODO - verify whether feedEntry has content, so it doesn't need to be re-downloaded (like Brasil - Exame)
		LOGGER.info("Processing feed entry {} - {} '{}'", feedEntry.getPublishedDate(), feedEntry.getTitle(), feedEntry.getLink());
		String feedEntryContent;
		try {
			feedEntryContent = readFeedEntryContent(feedEntry.getLink());
		} catch (Exception e) {
			LOGGER.error("Exception while reading feed content", e);
			return;
		}
		final RawFeedEntryVO rawFeedEntryVO = new RawFeedEntryVO(
				null, feedEntry.getUri(), feedEntry.getTitle(), feedEntry.getLink(), 
				feedEntry.getPublishedDate() == null ? null : feedEntry.getPublishedDate().getTime(), 
				feedEntryContent, feedGroupEntryId, null);
		rawFeedEntryClient.create(rawFeedEntryVO);
		processedFeedEntryUris.add(feedEntry.getUri());
	}

	private String readFeedEntryContent(final String link) {
		LOGGER.info("Reading content from {}", link);
		URL url;
		try {
			url = new URL(link);
		} catch (MalformedURLException e1) {
			throw new IllegalArgumentException(e1);
		}
		final StringBuilder sb = new StringBuilder();
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
            	sb.append(inputLine);
            }
        } catch (IOException e) {
        	throw new RuntimeException(e);
		} 
		return sb.toString();
	}
	
}
