package com.cognitive.newswizard.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cognitive.newswizard.service.entity.ProperNounEntity;

public interface ProperNounRepository extends MongoRepository<ProperNounEntity, String> {

}
