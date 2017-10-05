package com.cognitive.newswizard.service.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.junit.Test;

public class DateTimeTest {

	@Test
	public void testDateToLong() {
		final Date currentDate = new Date();
		
		final LocalDateTime localDateTimeNow = LocalDateTime.now();
		final LocalDateTime localDateTime1 = LocalDateTime.of(2017, 9, 17, 14, 51, 00);
		final LocalDateTime localDateTime2 = LocalDateTime.of(2017, 9, 17, 0, 0, 0);
		final LocalDateTime localDateTime3 = LocalDateTime.of(2017, 9, 10, 0, 0, 0);
		
		System.out.println("Current date time:\t" + currentDate + "\t\t" + currentDate.getTime());
		System.out.println("localDateTimeNow:\t" + localDateTimeNow.toString() + "\t\t\t" + Timestamp.valueOf(localDateTimeNow).getTime());
		System.out.println("localDateTime1:\t\t" + localDateTime1.toString() + "\t\t\t" + Timestamp.valueOf(localDateTime1).getTime());
		System.out.println("localDateTime2:\t\t" + localDateTime2.toString() + "\t\t\t" + Timestamp.valueOf(localDateTime2).getTime());
		System.out.println("localDateTime3:\t\t" + localDateTime3.toString() + "\t\t\t" + Timestamp.valueOf(localDateTime3).getTime());
		
		System.out.println();
	}
	
	@Test
	public void testLongToDate() {
		
		final Long val1 = 1495984558000L;
		final Long val2 = 1490997600000L;
		final Long val3 = 1491083999999L;
		
		System.out.println("val1 =\t" + val1 + "\t" + new Date(val1));
		System.out.println("val2 =\t" + val2 + "\t" + new Date(val2));
		System.out.println("val3 =\t" + val3 + "\t" + new Date(val3));
		
		final Date now = new Date();
		System.out.println(now.getTime());
		System.out.println(LocalDateTime.now().toInstant(ZoneOffset.ofHours(2)).toEpochMilli());
		
		
		System.out.println();
	}
}
