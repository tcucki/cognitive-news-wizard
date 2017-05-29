package com.cognitive.newswizard.agent.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.cognitive.newswizard.api.vo.newsfeed.FeedGroupVO;

@Component
public class FeedGroupClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FeedGroupClient.class);
	
	private final String serviceServerURL;

	private final RestTemplate restTemplate;

	public FeedGroupClient(@Value("${newswizard.serviceserver.url}") final String serviceServerURL) {
		this.serviceServerURL = serviceServerURL + "feedgroup/";
		restTemplate = new RestTemplate();
	}
	
	public FeedGroupVO[] getAll() {
		LOGGER.info("Retrieving all feed groups");
		return restTemplate.getForObject(serviceServerURL, FeedGroupVO[].class);
	}
}
