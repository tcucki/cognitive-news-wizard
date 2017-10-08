package com.cognitive.newswizard.service.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cognitive.newswizard.api.vo.newsfeed.FeedSourceGroupVO;
import com.cognitive.newswizard.service.service.FeedSourceGroupService;

@Controller
@RequestMapping("/feedsourcegroup")
public class FeedSourceGroupResource {

	private final FeedSourceGroupService feedSourceGroupService;

	@Autowired
	public FeedSourceGroupResource(final FeedSourceGroupService feedSourceGroupService) {
		this.feedSourceGroupService = feedSourceGroupService;
	}
	
	@RequestMapping(method=RequestMethod.POST, path = "/create")
	@ResponseBody
	public FeedSourceGroupVO create(@RequestBody final FeedSourceGroupVO feedSourceGroupVO) {
		return feedSourceGroupService.create(feedSourceGroupVO);
	}
	
	@RequestMapping(method=RequestMethod.GET, path = "/")
	@ResponseBody
	public List<FeedSourceGroupVO> getAll() {
		return feedSourceGroupService.getAll();
	}
}
