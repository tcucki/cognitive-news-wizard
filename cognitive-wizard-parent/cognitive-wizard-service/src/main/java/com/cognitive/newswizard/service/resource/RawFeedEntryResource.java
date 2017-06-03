package com.cognitive.newswizard.service.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cognitive.newswizard.api.vo.newsfeed.RawFeedEntryVO;
import com.cognitive.newswizard.service.service.RawFeedEntryService;

@Controller
@RequestMapping("/rawfeedentry")
public class RawFeedEntryResource {

	private final RawFeedEntryService rawFeedEntryService;

	@Autowired
	public RawFeedEntryResource(RawFeedEntryService rawFeedEntryService) {
		this.rawFeedEntryService = rawFeedEntryService;
	}
	
	@RequestMapping(method=RequestMethod.POST, path = "/create")
	@ResponseBody
	public RawFeedEntryVO create(@RequestBody final RawFeedEntryVO rawFeedEntryVO) {
		// do not return compacted content
		final RawFeedEntryVO persisted = rawFeedEntryService.create(rawFeedEntryVO);
		persisted.setContent(rawFeedEntryVO.getContent());
		persisted.setCompactContent(null);
		return persisted;
	}
	
	@RequestMapping(method=RequestMethod.POST, path = "/compactall")
	@ResponseBody
	public String compactAllFeeds() {
		rawFeedEntryService.compactContentAllEntries();
		return "ok";
	}

}
