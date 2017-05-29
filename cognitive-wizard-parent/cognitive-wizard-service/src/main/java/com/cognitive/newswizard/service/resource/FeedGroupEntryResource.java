package com.cognitive.newswizard.service.resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cognitive.newswizard.api.vo.newsfeed.CreateFeedGroupEntryParamsVO;
import com.cognitive.newswizard.api.vo.newsfeed.FeedGroupEntryVO;
import com.cognitive.newswizard.service.service.FeedGroupEntryService;

@Controller
@RequestMapping("/feedgroupentry")
public class FeedGroupEntryResource {

	private final FeedGroupEntryService feedGroupEntryService;

	public FeedGroupEntryResource(FeedGroupEntryService feedGroupEntryService) {
		super();
		this.feedGroupEntryService = feedGroupEntryService;
	}

	@RequestMapping(method = RequestMethod.POST, path = "/create")
	@ResponseBody
	public FeedGroupEntryVO create(
			@RequestBody final CreateFeedGroupEntryParamsVO createParamsVO) {
		final FeedGroupEntryVO feedGroupEntryVO = new FeedGroupEntryVO(
				createParamsVO.getId(), createParamsVO.getName(),
				createParamsVO.getUrl(), null);
		return feedGroupEntryService.create(feedGroupEntryVO, createParamsVO.getFeedGroupId());
	}

}
