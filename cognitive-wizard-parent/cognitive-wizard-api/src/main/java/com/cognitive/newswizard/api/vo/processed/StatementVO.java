package com.cognitive.newswizard.api.vo.processed;

import org.apache.commons.lang3.StringUtils;

/**
 * Represents an statement extract from a feed, with some pre-processing
 * @author tiago
 *
 */
public class StatementVO {

	private final String original;

	private final String noAccents;
	
	private final String upperCase;
	
	public StatementVO(final String original) {
		this.original = original;
		this.noAccents = StringUtils.stripAccents(original);
		this.upperCase = noAccents.toUpperCase();
	}

	public String getOriginal() {
		return original;
	}

	public String getNoAccents() {
		return noAccents;
	}

	public String getUpperCase() {
		return upperCase;
	}
	
	/**
	 * 
	 * @return final statement after tidying. Currently it is the upperCase value
	 */
	public String getStatement() {
		return upperCase;
	}

}
