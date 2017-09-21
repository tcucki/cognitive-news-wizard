package com.cognitive.newswizard.service.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.cognitive.newswizard.service.entity.RawFeedEntryEntity;
import com.cognitive.newswizard.service.repository.RawFeedEntryAggregationRepository;
import com.cognitive.newswizard.service.repository.projection.CountRawFeedEntryByPeriodProjection;

@Component
public class RawFeedEntryAggregationRepositoryImpl implements RawFeedEntryAggregationRepository {
	
	private static final String COLLECTION_NAME = "raw_feed_entry";
	

	private final MongoTemplate mongoTemplate;
	
	@Autowired
	public RawFeedEntryAggregationRepositoryImpl(final MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}


	@Override
	public List<CountRawFeedEntryByPeriodProjection> countRawFeedEntryByPeriod(final Long start, final Long end) {
		
		final Aggregation aggregation = Aggregation.newAggregation(
				
				RawFeedEntryEntity.class,
				Aggregation.match(Criteria.where("publishedDateTime").gte(start).lte(end)),
				Aggregation.group("feedGroupEntryId").count().as("count")
		);
		
		return mongoTemplate.aggregate(aggregation, COLLECTION_NAME, CountRawFeedEntryByPeriodProjection.class).getMappedResults();
	}

}
