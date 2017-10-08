package com.cognitive.newswizard.agent.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.cognitive.newswizard.api.vo.newsfeed.FeedSourceGroupVO;

@Component
public class FeedSourceGroupClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FeedSourceGroupClient.class);
	
	private final String serviceServerURL;

	private final RestTemplate restTemplate;

	public FeedSourceGroupClient(@Value("${newswizard.serviceserver.url}") final String serviceServerURL) {
		this.serviceServerURL = serviceServerURL + "feedsourcegroup/";
		restTemplate = new RestTemplate();
	}
	
	public FeedSourceGroupVO[] getAll() {
		LOGGER.info("Retrieving all feed groups");
		return restTemplate.getForObject(serviceServerURL, FeedSourceGroupVO[].class);
	}
}
