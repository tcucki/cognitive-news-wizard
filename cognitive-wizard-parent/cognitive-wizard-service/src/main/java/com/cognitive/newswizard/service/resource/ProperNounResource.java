package com.cognitive.newswizard.service.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cognitive.newswizard.api.vo.processed.MissingProperNounVO;
import com.cognitive.newswizard.service.service.ProperNounService;

@Controller
@RequestMapping("/propernoun")
public class ProperNounResource {

	private final ProperNounService properNounService;

	@Autowired
	public ProperNounResource(final ProperNounService properNounService) {
		this.properNounService = properNounService;
	}

	@RequestMapping(method=RequestMethod.GET, path = "/missing")
	@ResponseBody
	public List<MissingProperNounVO> reportAllMissingProperNouns(
			@RequestParam final boolean lookInParagraphs, 
			@RequestParam final int countThreshold) {
		return properNounService.reportAllMissingProperNouns(lookInParagraphs, countThreshold);
	}

	@RequestMapping(method=RequestMethod.GET, path = "/missing/report")
	@ResponseBody
	public String reportAllMissingProperNounsReport(
			@RequestParam final boolean lookInParagraphs, 
			@RequestParam final int countThreshold) {
		final List<MissingProperNounVO> list = properNounService.reportAllMissingProperNouns(lookInParagraphs, countThreshold);
		final StringBuilder sb = new StringBuilder();
		for (MissingProperNounVO item : list) {
			sb.append(
					item.getCount()).append(";")
					.append(item.getProperNoun()).append(";")
					.append(item.getSuggestion() == null ? "" : item.getSuggestion()).append('\n');
		}
		return sb.toString();
	}
}
