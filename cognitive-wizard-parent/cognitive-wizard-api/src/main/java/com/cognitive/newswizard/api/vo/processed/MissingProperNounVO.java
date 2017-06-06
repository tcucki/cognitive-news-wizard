package com.cognitive.newswizard.api.vo.processed;

import java.util.Comparator;

public class MissingProperNounVO {

	private final int count;
	
	private final String properNoun;
	
	private final String suggestion;

	public MissingProperNounVO(int count, String properNoun, String suggestion) {
		super();
		this.count = count;
		this.properNoun = properNoun;
		this.suggestion = suggestion;
	}

	public int getCount() {
		return count;
	}

	public String getProperNoun() {
		return properNoun;
	}

	public String getSuggestion() {
		return suggestion;
	}
	
	public static class MissingProperNounComparator implements Comparator<MissingProperNounVO> {

		public int compare(MissingProperNounVO o1, MissingProperNounVO o2) {
			if (o1.getCount() < o2.getCount()) {
				return 1;
			} else {
				if (o1.getCount() == o2.getCount()) {
					return 0;
				}
			}
			return -1;
		}
		
	}

}
