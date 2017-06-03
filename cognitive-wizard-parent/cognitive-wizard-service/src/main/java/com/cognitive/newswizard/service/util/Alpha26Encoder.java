package com.cognitive.newswizard.service.util;

import org.apache.commons.lang3.StringUtils;

import com.cognitive.newswizard.api.vo.AbstractValueObject;

public class Alpha26Encoder {
	
	private static final long BASE = 26l;
	
	private static final long FIRST = 'A' -1;

	@SuppressWarnings("serial")
	public static class EncodeInfo extends AbstractValueObject {
		
		private static final int MAX_SIZE = 13;

		private final String original;
		
		private final String fitted;
		
		private final String noAccents;
		
		private final String upperCase;
		
		private final long encoded;
		
		private String decodedUppercase;
		
		public EncodeInfo(final String original) {
			this.original = original;
			if (original.length() > MAX_SIZE) {
				fitted = original.substring(0, MAX_SIZE);
			} else {
				fitted = original;
			}
			noAccents = StringUtils.stripAccents(fitted);
			upperCase = noAccents.toUpperCase();
			encoded = internalEncode(this.upperCase);
			decodedUppercase = decode(encoded);
		}

		public String getOriginal() {
			return original;
		}
		
		public String getFitted() {
			return fitted;
		}

		public String getNoAccents() {
			return noAccents;
		}

		public String getUpperCase() {
			return upperCase;
		}

		public long getEncoded() {
			return encoded;
		}

		public String getDecodedUppercase() {
			return decodedUppercase;
		}
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			final EncodeInfo other = (EncodeInfo) obj;
			return encoded == other.encoded;
		}
		@Override
		public int hashCode() {
			return ((Long) encoded).hashCode();
		}
	}
	
	public static long encode(final String value) {
		return new EncodeInfo(value).encoded;
	}
	
	public static String decode(final long encoded) {
		if (encoded == -1) {
			return "";
		}
		long x = encoded;
		String value = "";
		
		while (x > BASE) {
			long r = (x % BASE);
			if (r == 0l) {
				value += ' ';
			} else {
				value += (char) (r + FIRST);
			}
			x = (x / BASE);
		}
		value += (char) (x + FIRST);
		return value;
	}

	private static long internalEncode(final String upperCaseValue) {
		if (StringUtils.isBlank(upperCaseValue)) {
			return -1;
		}
		long encoded = 0;
		for (int i = 0; i < upperCaseValue.length(); i++) {
			char ch = upperCaseValue.charAt(i);
			if (ch != ' ') {
				long c = ch - FIRST;
				encoded += Math.round(Math.pow(BASE, i)) * c;
			}
		}
		return encoded;
	}
}
