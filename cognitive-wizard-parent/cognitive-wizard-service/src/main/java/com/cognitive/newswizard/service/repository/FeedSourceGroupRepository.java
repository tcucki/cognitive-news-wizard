package com.cognitive.newswizard.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cognitive.newswizard.service.entity.FeedSourceGroupEntity;

public interface FeedSourceGroupRepository extends MongoRepository<FeedSourceGroupEntity, String> {

}
