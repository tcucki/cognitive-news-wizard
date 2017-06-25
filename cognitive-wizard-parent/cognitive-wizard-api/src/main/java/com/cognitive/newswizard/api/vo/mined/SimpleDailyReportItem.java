package com.cognitive.newswizard.api.vo.mined;

import java.time.LocalDate;

/**
 * Contains the number a proper noun is counted by date
 * @author tiago
 *
 */
public class SimpleDailyReportItem {

	private final String properNoun;
	
	private final LocalDate date;
	
	private Long count = 0L;

	public SimpleDailyReportItem(String properNoun, LocalDate date) {
		this.properNoun = properNoun;
		this.date = date;
	}

	public String getProperNoun() {
		return properNoun;
	}

	public LocalDate getDate() {
		return date;
	}

	public Long getCount() {
		return count;
	}
	
	public void increment() {
		count++;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((properNoun == null) ? 0 : properNoun.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleDailyReportItem other = (SimpleDailyReportItem) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (properNoun == null) {
			if (other.properNoun != null)
				return false;
		} else if (!properNoun.equals(other.properNoun))
			return false;
		return true;
	}
}
