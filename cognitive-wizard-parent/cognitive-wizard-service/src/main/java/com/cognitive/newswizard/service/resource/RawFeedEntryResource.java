package com.cognitive.newswizard.service.resource;

import static com.cognitive.newswizard.service.util.DateTimeUtil.basicIsoDateStringToLocalDate;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cognitive.newswizard.api.vo.newsfeed.RawFeedEntryVO;
import com.cognitive.newswizard.api.vo.newsfeed.RawFeedReportItem;
import com.cognitive.newswizard.service.service.RawFeedEntryService;

@Controller
@RequestMapping("/rawfeedentry")
public class RawFeedEntryResource {
	
	private final Logger LOGGER = LoggerFactory.getLogger(RawFeedEntryResource.class);

	private final RawFeedEntryService rawFeedEntryService;

	@Autowired
	public RawFeedEntryResource(RawFeedEntryService rawFeedEntryService) {
		this.rawFeedEntryService = rawFeedEntryService;
	}

	@RequestMapping(method = RequestMethod.POST, path = "/create")
	@ResponseBody
	public RawFeedEntryVO create(@RequestBody final RawFeedEntryVO rawFeedEntryVO) {
		try {
			// do not return compacted content
			final RawFeedEntryVO persisted = rawFeedEntryService.create(rawFeedEntryVO);
			persisted.setContent(rawFeedEntryVO.getContent());
			persisted.setCompactContent(null);
			return persisted;
		} catch (RuntimeException e) {
			LOGGER.error("Uncaught exception on resource provider: {}", e);
			throw e;
		}
	}

	@RequestMapping(method = RequestMethod.POST, path = "/compactall")
	@ResponseBody
	public String compactAllFeeds() {
		rawFeedEntryService.compactContentAllEntries();
		return "ok";
	}

	@RequestMapping(method = RequestMethod.GET, path = "/report/period")
	@ResponseBody
	public List<RawFeedReportItem> getRawFeedReportItemReport(@RequestParam final String start, @RequestParam final String end) {

		return rawFeedEntryService.getRawFeedReportItemReport(
				basicIsoDateStringToLocalDate(start),
				basicIsoDateStringToLocalDate(end));
	}

}
