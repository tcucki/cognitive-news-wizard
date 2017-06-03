package com.cognitive.newswizard.service.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cognitive.newswizard.service.service.FeedProcessor;

@Controller
@RequestMapping("/feedprocessor")
public class FeedProcessorResource {

	private final FeedProcessor feedProcessor;

	@Autowired
	public FeedProcessorResource(FeedProcessor feedProcessor) {
		this.feedProcessor = feedProcessor;
	}
	
	@RequestMapping(method=RequestMethod.POST, path = "/execute")
	@ResponseBody
	public String execute() {
		feedProcessor.processAllFeeds();
		return "ok";
	}
	
}
