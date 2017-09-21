package com.cognitive.newswizard.service.repository;

import java.util.List;

import com.cognitive.newswizard.service.repository.projection.CountRawFeedEntryByPeriodProjection;

public interface RawFeedEntryAggregationRepository {

	List<CountRawFeedEntryByPeriodProjection> countRawFeedEntryByPeriod(final Long start, final Long end);
}
