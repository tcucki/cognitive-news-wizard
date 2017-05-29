package com.cognitive.newswizard.service.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ZipUtils.class);

	public static String uncompress(final byte[] compressed) {
		if (compressed == null || compressed.length == 0) {
			return null;
		}
		try (final GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressed))) {
			final BufferedReader bf = new BufferedReader(new InputStreamReader(gis));
			String outStr = "";
			String line;
			while ((line = bf.readLine()) != null) {
				outStr += line;
			}
			return outStr;
		} catch (IOException ioe) {
			LOGGER.error("Error uncompressing data", ioe);
			return null;
		}
	}

	public static byte[] compress(final String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		
		try {
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			final GZIPOutputStream gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes());
			gzip.close();
			return out.toByteArray();
		} catch (IOException ioe) {
			LOGGER.error("Error compressing data", ioe);
			return null;
		}
   	}
}
