package com.cognitive.newswizard.agent.rss;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

public class RomeTest {

	@Test
	public void test() throws IllegalArgumentException, MalformedURLException, FeedException, IOException {
//		final String url = "http://stackoverflow.com/feeds/tag?tagnames=rome";
//		final String url = "http://rss.cnn.com/rss/cnn_latest.rss";
//		final String url = "http://feeds.bbci.co.uk/news/rss.xml";
//		final String url = "https://news.google.com/?output=rss";
//		final String url = "http://feeds.reuters.com/Reuters/worldNews";
		final String url = "http://feeds.folha.uol.com.br/poder/rss091.xml";
		final SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(url)));
		System.out.println(feed.getTitle());
		System.out.println(feed.getDescription());
		System.out.println("***");
		for (SyndEntry entry : feed.getEntries()) {
//			System.out.println(entry.);
			System.out.println(entry.getTitle());
			System.out.println(entry.getLink());
			System.out.println(entry.getPublishedDate());
			System.out.println(entry.getContents().size());
			for (SyndContent content : entry.getContents()) {
				System.out.println(content.getValue());
			}
			System.out.println("-----------------------------");
		}
		System.out.println(feed.getEntries().size());
	}
}
