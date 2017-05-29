package com.cognitive.newswizard.agent.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.cognitive.newswizard.api.vo.newsfeed.RawFeedEntryVO;

@Component
public class RawFeedEntryClient {

	private final String serviceServerURL;

	private final RestTemplate restTemplate;

	public RawFeedEntryClient(@Value("${newswizard.serviceserver.url}") final String serviceServerURL) {
		this.serviceServerURL = serviceServerURL + "rawfeedentry/";;
		restTemplate = new RestTemplate();
	}

	public RawFeedEntryVO create(final RawFeedEntryVO rawFeedEntryVO) {
		return restTemplate.postForObject(serviceServerURL + "/create", rawFeedEntryVO, RawFeedEntryVO.class);
	}
}
