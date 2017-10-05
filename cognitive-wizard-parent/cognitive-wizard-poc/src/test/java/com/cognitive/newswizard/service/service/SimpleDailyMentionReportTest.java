package com.cognitive.newswizard.service.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cognitive.newswizard.api.vo.mined.SimpleDailyReportItem;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleDailyMentionReportTest {

	@Autowired
	private MentionService mentionService;
	
	@Test
	public void test() {
		final LocalDate start = LocalDate.of(2017, 5, 1);
		final LocalDate end = LocalDate.of(2017, 6, 24);
		final List<SimpleDailyReportItem> items = mentionService.getSimpleDailyReport(start, end);
		System.out.println(items.size());
		
		Collections.sort(items, new Comparator<SimpleDailyReportItem>() {

			@Override
			public int compare(SimpleDailyReportItem o1, SimpleDailyReportItem o2) {
				if (o1.getDate().equals(o2.getDate())) {
					return o1.getProperNoun().compareTo(o2.getProperNoun());
				}
				return o1.getDate().compareTo(o2.getDate());
			}
		});
		
		for (SimpleDailyReportItem item : items) {
			System.out.println(item.getDate() + "\t" + item.getProperNoun() + "\t" + item.getCount());
		}
	}
}
