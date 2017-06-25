package com.cognitive.newswizard.service.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cognitive.newswizard.service.entity.FeedTextEntity;
import com.cognitive.newswizard.service.repository.FeedTextRepository;
import com.cognitive.newswizard.service.util.Alpha26Encoder;

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
		while (feedTextPages.hasNext()) {
			feedTextPages = feedTextRepository.findAll(feedTextPages.nextPageable());
			feedTextPages.forEach(feed -> processFeed(feed));
		}
		
//		properNouns.forEach((k, v) -> System.out.format("%4d - %s\n", v, k));
		System.out.println("Converting...");
		List<ProperNoumCounter> properNoumCounterList = new ArrayList<>();
		properNouns.forEach((k, v) -> properNoumCounterList.add(new ProperNoumCounter(v, k)));
		
		System.out.println("Ordering...");
		properNoumCounterList.sort(new ProperNoumCounterComparator());
		
		
		properNoumCounterList.forEach(value -> {
			if (value.count >= 9) {
				System.out.format("%d;%s;%s;%s;%s;%d;%s\n", 
					value.count, 
					value.encoded.getOriginal(), 
					value.encoded.getFitted(),
					value.encoded.getNoAccents(),
					value.encoded.getUpperCase(),
					value.encoded.getEncoded(),
					value.encoded.getDecodedUppercase());
			}
		});
		
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
		final Matcher matcher = PATTERN.matcher(StringUtils.stripAccents(statement));
		while (matcher.find()) {
			final String properNoun = matcher.group().trim();
			addToMap(properNoun);
//			System.out.println(properNoun);
		}
	}

	private void addToMap(String properNoun) {
		properNouns.compute(properNoun, (k, v) -> v == null ? 1 : ++v);
	}
	
	public class ProperNoumCounter {
		private int count;
		private Alpha26Encoder.EncodeInfo encoded;
		public ProperNoumCounter(final int count, final String name) {
			this.count = count;
			this.encoded = new Alpha26Encoder.EncodeInfo(name);
		}
		public int getCount() {
			return count;
		}
		public Alpha26Encoder.EncodeInfo getEncoded() {
			return encoded;
		}
	}
	
	class ProperNoumCounterComparator implements Comparator<ProperNoumCounter> {

		@Override
		public int compare(ProperNoumCounter o1, ProperNoumCounter o2) {
			if (o1.count < o2.count) {
				return 1;
			} else {
				if (o1.count == o2.count) {
					return 0;
				}
			}
			return -1;
		}
		
	}
}
