package com.cognitive.newswizard.service.util;

import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class DateTimeUtil {
	
	private static ZoneOffset CURRENT_ZONE_OFFSET = ZoneId.systemDefault().getRules().getOffset(Instant.now());

	public static LocalDate basicIsoDateStringToLocalDate(final String date) {
		return BASIC_ISO_DATE.parse(date, LocalDate::from);
	}
	
	public static long localDateToTimeMilis(final LocalDate date, final boolean beginingOfDay) {
		if (beginingOfDay) {
			return date.atTime(0, 0, 0, 0).toInstant(CURRENT_ZONE_OFFSET).toEpochMilli();
		}
		return date.atTime(23, 59, 59, 999999999).toInstant(CURRENT_ZONE_OFFSET).toEpochMilli();
	}
}
