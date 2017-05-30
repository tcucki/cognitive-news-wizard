package com.cognitive.newswizard.service.service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cognitive.newswizard.service.entity.FeedTextEntity;
import com.cognitive.newswizard.service.repository.FeedTextRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NameExtractorServiceTest {
	
	private static final Pattern PATTERN = Pattern.compile("(?:\\s*\\b([A-Z][A-Za-z]+)\\b)+");
	
	private final Map<String, Integer> properNouns = new HashMap<>();
	
	private int feedsProcessed = 0;

	@Autowired
	private FeedTextRepository feedTextRepository;
	
	@Test
	public void test_extractNames() {

		Page<FeedTextEntity> feedTextPages = feedTextRepository.findAll(new PageRequest(0, 1000));
		System.out.format("*** Reading %d elements on %d pages \n", feedTextPages.getTotalElements(), feedTextPages.getTotalPages());
		
		feedTextPages.forEach(feed -> processFeed(feed));
		if (feedTextPages.hasNext()) {
			feedTextPages = feedTextRepository.findAll(feedTextPages.nextPageable());
		}
		feedTextPages.forEach(feed -> processFeed(feed));
		
		properNouns.forEach((k, v) -> System.out.format("%4d - %s\n", v, k));
		
		System.out.println("\nTotal proper nouns found: " + properNouns.size());
		System.out.println("\nTotal feeds processed: " + feedsProcessed);
	}
	
	private void processFeed(final FeedTextEntity entity) {
//		System.out.println("---------------- TITLE -------------------");
		extractProperName(entity.getTitle());
//		System.out.println("---------------- SUB-TITLES -------------------");
//		entity.getSubTitles().forEach(paragraph -> extractProperName(paragraph));
//		System.out.println("---------------- PARAGRAPHS -------------------");
		entity.getParagraphs().forEach(paragraph -> extractProperName(paragraph));
		feedsProcessed++;
	}

	private void extractProperName(String statement) {
//		System.out.println("*** Statement: '" + statement + "'");
		final Matcher matcher = PATTERN.matcher(statement);
		while (matcher.find()) {
			final String properNoun = matcher.group().trim();
			addToMap(properNoun);
//			System.out.println(properNoun);
		}
	}

	private void addToMap(String properNoun) {
		properNouns.compute(properNoun, (k, v) -> v == null ? 1 : ++v);
	}
}
