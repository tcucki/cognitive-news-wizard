package com.cognitive.newswizard.api.vo;

import java.io.Serializable;

import com.google.gson.Gson;

@SuppressWarnings("serial")
public class AbstractValueObject implements Serializable {

	private static final Gson GSON = new Gson();
	
	@Override
	public String toString() {
		return GSON.toJson(this);
	}
}
