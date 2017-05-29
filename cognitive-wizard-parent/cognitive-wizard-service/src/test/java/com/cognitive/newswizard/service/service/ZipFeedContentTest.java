package com.cognitive.newswizard.service.service;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cognitive.newswizard.service.entity.RawFeedEntryEntity;
import com.cognitive.newswizard.service.repository.RawFeedEntryRepository;
import com.cognitive.newswizard.service.util.ZipUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZipFeedContentTest {

	@Autowired
	private RawFeedEntryRepository rawFeedEntryRepository;

	@Test
	public void zip1EntryTest() throws IOException {
		final RawFeedEntryEntity entity = rawFeedEntryRepository.findOne("5922095ac0fe5e1245a9a0ef");
		final String originalContent = entity.getContent();
		System.out.println(originalContent.length());
		final byte[] compressed = ZipUtils.compress(originalContent);
		System.out.println(compressed.length);
		System.out.println((compressed.length * 1d) / (entity.getContent().length() * 1d));
		final String uncompressed = ZipUtils.uncompress(compressed);
		Assert.assertEquals(originalContent, uncompressed);
	}

	@Test
	public void zipAllTest() {
		final List<RawFeedEntryEntity> allRawEntryIds = rawFeedEntryRepository.findAllIds();
		System.out.println("Total feeds to compact: " + allRawEntryIds.size());
		int i = 0;
		for (final RawFeedEntryEntity rawFeedEntryIdEntity : allRawEntryIds) {
			i++;
			final RawFeedEntryEntity actualRawFeedEntryEntity = rawFeedEntryRepository.findOne(rawFeedEntryIdEntity.getId());
			final String originalContent = actualRawFeedEntryEntity.getContent();
			if (originalContent == null) {
				continue;
			}
			final byte[] compressedContent = ZipUtils.compress(originalContent);
			final String uncompressedContent = ZipUtils.uncompress(compressedContent);
			if (!originalContent.equals(uncompressedContent)) {
				throw new RuntimeException("Uncompressed content not equal to original content for entry id " + actualRawFeedEntryEntity.getId());
			}
			final double ratio = (compressedContent.length * 1d) / (originalContent.length() * 1d);
			System.out.println(String.format("Entry # %4d - %.2f compact ratio", i, ratio * 100));
		}
	}
	
	@Test
	public void testFormat() {
		System.out.format("%4d - %.2f", 1, 21.43d);
//		String.format("%4d - %.2f", 10, 21.43d);
	}
}
