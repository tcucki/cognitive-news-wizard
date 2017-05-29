package com.cognitive.newswizard.service.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cognitive.newswizard.api.vo.newsfeed.FeedGroupVO;
import com.cognitive.newswizard.service.service.FeedGroupService;

@Controller
@RequestMapping("/feedgroup")
public class FeedGroupResource {

	private final FeedGroupService feedGroupService;

	@Autowired
	public FeedGroupResource(final FeedGroupService feedGroupService) {
		this.feedGroupService = feedGroupService;
	}
	
	@RequestMapping(method=RequestMethod.POST, path = "/create")
	@ResponseBody
	public FeedGroupVO create(@RequestBody final FeedGroupVO feedGroupVO) {
		return feedGroupService.create(feedGroupVO);
	}
	
	@RequestMapping(method=RequestMethod.GET, path = "/")
	@ResponseBody
	public List<FeedGroupVO> getAll() {
		return feedGroupService.getAll();
	}
}
