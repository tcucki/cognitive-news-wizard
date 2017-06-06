package com.cognitive.newswizard.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cognitive.newswizard.service.entity.IgnoreNounEntity;

public interface IgnoreNounRepository extends MongoRepository<IgnoreNounEntity, String> {

}
