package com.cognitive.newswizard.service.service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cognitive.newswizard.api.vo.newsfeed.RawFeedEntryVO;
import com.cognitive.newswizard.service.entity.RawFeedEntryEntity;
import com.cognitive.newswizard.service.repository.RawFeedEntryRepository;
import com.cognitive.newswizard.service.translator.RawFeedEntryTranslator;
import com.cognitive.newswizard.service.util.ZipUtils;

@Component
public class RawFeedEntryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RawFeedEntryService.class);
	
	private final NumberFormat numberFormat = new DecimalFormat("#0.000");   

	private final RawFeedEntryRepository rawFeedEntryRepository;

	private final FeedProcessor feedProcessor;

	@Autowired
	public RawFeedEntryService(
			final RawFeedEntryRepository rawFeedEntryRepository,
			final FeedProcessor feedProcessor) {
		this.rawFeedEntryRepository = rawFeedEntryRepository;
		this.feedProcessor = feedProcessor;
	}

	public RawFeedEntryVO create(final RawFeedEntryVO rawFeedEntryVO) {
		final RawFeedEntryEntity existingEntity = rawFeedEntryRepository.findByFeedEntryId(rawFeedEntryVO.getFeedEntryId());
		if (existingEntity != null) {
			LOGGER.info("Skipping {} as it is alredy registered", rawFeedEntryVO.getFeedEntryId());
			return RawFeedEntryTranslator.toValueObject(existingEntity);
		}
		LOGGER.info("Creating new raw feed entry {}", rawFeedEntryVO.getTitle());
		rawFeedEntryVO.setCompactContent(ZipUtils.compress(rawFeedEntryVO.getContent()));
		rawFeedEntryVO.setContent(null);
		final RawFeedEntryEntity entity = RawFeedEntryTranslator.toEntity(rawFeedEntryVO);
		final RawFeedEntryVO persisted = RawFeedEntryTranslator.toValueObject(rawFeedEntryRepository.save(entity));
		feedProcessor.processFeed(rawFeedEntryVO);
		return persisted;
	}
	
	public void compactContentAllEntries() {
		final Long start = System.currentTimeMillis();
		final List<RawFeedEntryEntity> allRawEntryIds = rawFeedEntryRepository.findAllIds();
		LOGGER.info("Total feeds to compact: " + allRawEntryIds.size());
		int counter = 0;
		for (final RawFeedEntryEntity rawFeedEntryIdEntity : allRawEntryIds) {
			counter++;
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
			final RawFeedEntryEntity compactedRawFeedEntryEntity = new RawFeedEntryEntity(
					actualRawFeedEntryEntity.getId(), 
					actualRawFeedEntryEntity.getFeedEntryId(),
					actualRawFeedEntryEntity.getTitle(),
					actualRawFeedEntryEntity.getAddress(), 
					actualRawFeedEntryEntity.getPublishedDateTime(), 
					null, 
					actualRawFeedEntryEntity.getFeedGroupEntryId(), 
					compressedContent);
			rawFeedEntryRepository.save(compactedRawFeedEntryEntity);
			LOGGER.info(String.format("Entry # %4d - %.2f compact ratio", counter, ratio * 100));
		}
		final Double seconds = (System.currentTimeMillis() - start) / 1000d;
		LOGGER.info("Finished compacting all raw feeds ({}) in {} seconds\n---------------------------------------------------------------------", counter, numberFormat.format(seconds));
	}
}