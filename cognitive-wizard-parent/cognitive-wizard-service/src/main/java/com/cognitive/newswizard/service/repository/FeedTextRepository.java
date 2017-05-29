package com.cognitive.newswizard.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cognitive.newswizard.service.entity.FeedTextEntity;

public interface FeedTextRepository extends MongoRepository<FeedTextEntity, String> {

}
