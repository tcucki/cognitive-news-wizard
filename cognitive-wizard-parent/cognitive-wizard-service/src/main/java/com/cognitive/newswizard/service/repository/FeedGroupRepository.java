package com.cognitive.newswizard.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cognitive.newswizard.service.entity.FeedGroupEntity;

public interface FeedGroupRepository extends MongoRepository<FeedGroupEntity, String> {

}
