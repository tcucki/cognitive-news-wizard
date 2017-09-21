package com.cognitive.newswizard.service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.cognitive.newswizard.service.entity.RawFeedEntryEntity;

public interface RawFeedEntryRepository extends MongoRepository<RawFeedEntryEntity, String> {

	RawFeedEntryEntity findByFeedEntryId(final String feedEntryId);
	
	@Query(value="{}", fields="{_id : 1}")
	List<RawFeedEntryEntity> findAllIds();
	
	@Query(value="{_id : ?0}", fields="{publishedDateTime : 1}")
	RawFeedEntryEntity findPublishedDateTimeById(String id);
}
