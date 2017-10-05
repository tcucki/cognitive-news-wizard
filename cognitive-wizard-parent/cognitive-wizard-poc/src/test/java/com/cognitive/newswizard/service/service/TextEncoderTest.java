package com.cognitive.newswizard.service.service;

import java.math.BigInteger;
import java.text.Normalizer;

import org.junit.Test;

import com.cognitive.newswizard.service.util.Alpha26Encoder;
import com.cognitive.newswizard.service.util.Alpha26Encoder.EncodeInfo;

public class TextEncoderTest {

	@Test
	public void test() {
		BigInteger value = BigInteger.ZERO;
		final Long l = Long.MAX_VALUE;
		final BigInteger max = new BigInteger(l.toString());
		final long first = ' ';
		final long last = 'Z';
//		final long base = last - first;
		final long base = 27l;
		int index = 0;
		while (value.compareTo(max) < 0) {
//			final Long nextValue = index * base + base;
			final Long nextValue = Math.round(Math.pow(base, index)) * base;
			index++;
			if (nextValue < 0) {
				break;
			}
			value = value.add(new BigInteger(nextValue.toString()));
		}
		index--;
		System.out.println(index);
	}
	
	@Test
	public void test_encode() {
		Alpha26Encoder.EncodeInfo encodeInfo = new Alpha26Encoder.EncodeInfo("ãBç dEf");
		System.out.println(encodeInfo);
		encodeInfo = new Alpha26Encoder.EncodeInfo("abcdefghijklmnopqrstuvxyz");
		System.out.println(encodeInfo);
	}
}
