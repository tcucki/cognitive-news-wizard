package com.cognitive.newswizard.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cognitive.newswizard.service.entity.MentionEntity;

public interface MentionRepository extends MongoRepository<MentionEntity, String> {

}
