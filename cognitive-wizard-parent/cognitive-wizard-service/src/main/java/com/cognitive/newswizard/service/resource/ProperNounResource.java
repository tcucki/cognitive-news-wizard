package com.cognitive.newswizard.service.resource;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cognitive.newswizard.api.vo.processed.MissingProperNounVO;
import com.cognitive.newswizard.api.vo.processed.RegisterProperNounVO;
import com.cognitive.newswizard.service.service.ProperNounService;

@Controller
@RequestMapping("/propernoun")
public class ProperNounResource {
	private static final String BATCH_FIELD_SEPARATOR = ",";

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
					item.getCount()).append(BATCH_FIELD_SEPARATOR)
					.append(item.getProperNoun()).append(BATCH_FIELD_SEPARATOR)
					.append(item.getSuggestion() == null ? "" : item.getSuggestion()).append('\n');
		}
		return sb.toString();
	}
	
	@RequestMapping(method=RequestMethod.POST, path = "/missing/registerbatch")
	@ResponseBody
	public String registerProperNounBatch(@RequestBody final String content) {
		final List<RegisterProperNounVO> batch = readRegisterProperNounBatch(content);
		return properNounService.registerProperNounBatch(batch);
	}

	private List<RegisterProperNounVO> readRegisterProperNounBatch(final String content) {
		final List<RegisterProperNounVO> batch = new LinkedList<>();
		final String[] records = content.split("\n");
		for (final String record : records) {
			final String[] fields = record.split(BATCH_FIELD_SEPARATOR);
			if (fields.length < 3) {
				continue;
			}
			final String suggestion = fields[2];
			final String ignoreStr = fields.length == 3 ? null : fields[3];
			if (StringUtils.isNotBlank(suggestion) || StringUtils.isNotBlank(ignoreStr)) {
				batch.add(new RegisterProperNounVO(fields[1], suggestion, StringUtils.isNotBlank(ignoreStr)));
			}
		}
		return batch;
	}
}
