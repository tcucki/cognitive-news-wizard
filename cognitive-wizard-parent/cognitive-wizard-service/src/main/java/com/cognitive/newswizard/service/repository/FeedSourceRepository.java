package com.cognitive.newswizard.service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cognitive.newswizard.service.entity.FeedSourceEntity;

public interface FeedSourceRepository extends MongoRepository<FeedSourceEntity, String> {

	List<FeedSourceEntity> findByFeedSourceGroupId(final String feedSourceGroupId);
}
