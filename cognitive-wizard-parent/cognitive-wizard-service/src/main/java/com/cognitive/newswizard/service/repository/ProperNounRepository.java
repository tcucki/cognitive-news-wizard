package com.cognitive.newswizard.service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.cognitive.newswizard.service.entity.ProperNounEntity;

public interface ProperNounRepository extends MongoRepository<ProperNounEntity, String> {

	@Query(value="{}", fields="{rootNoun : 1, _id : 0}")
	List<String> findAllRootNouns();
}
