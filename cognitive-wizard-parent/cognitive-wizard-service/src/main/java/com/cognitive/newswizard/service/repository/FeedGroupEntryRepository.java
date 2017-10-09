package com.cognitive.newswizard.service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cognitive.newswizard.service.entity.FeedGroupEntryEntity;

public interface FeedGroupEntryRepository extends MongoRepository<FeedGroupEntryEntity, String> {

	List<FeedGroupEntryEntity> findByFeedSourceGroupId(final String feedSourceGroupId);
}
