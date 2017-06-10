package com.cognitive.newswizard.service.resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cognitive.newswizard.service.service.MentionService;

@Controller
@RequestMapping("/mention")
public class MentionResource {

	private final MentionService mentionService;
	
	public MentionResource(final MentionService mentionService) {
		this.mentionService = mentionService;
	}

	@RequestMapping(method=RequestMethod.POST, path = "/extractall")
	@ResponseBody
	public String extractAllMentions() {
		return mentionService.mineAllFeedsExtractMentioning();
	}
}
