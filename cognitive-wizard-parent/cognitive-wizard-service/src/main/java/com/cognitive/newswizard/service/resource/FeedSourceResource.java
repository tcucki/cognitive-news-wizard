package com.cognitive.newswizard.service.resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cognitive.newswizard.api.vo.newsfeed.CreateFeedSourceParamsVO;
import com.cognitive.newswizard.api.vo.newsfeed.FeedSourceVO;
import com.cognitive.newswizard.service.service.FeedSourceService;

@Controller
@RequestMapping("/feedsource")
public class FeedSourceResource {

	private final FeedSourceService feedSourceService;

	public FeedSourceResource(FeedSourceService feedSourceService) {
		super();
		this.feedSourceService = feedSourceService;
	}

	@RequestMapping(method = RequestMethod.POST, path = "/create")
	@ResponseBody
	public FeedSourceVO create(
			@RequestBody final CreateFeedSourceParamsVO createParamsVO) {
		final FeedSourceVO feedSourceVO = 
			new FeedSourceVO(
				createParamsVO.getId(), 
				createParamsVO.getName(),
				createParamsVO.getUrl(), 
				null,
				createParamsVO.getCode());
		return feedSourceService.create(feedSourceVO, createParamsVO.getFeedSourceGroupId());
	}

}
