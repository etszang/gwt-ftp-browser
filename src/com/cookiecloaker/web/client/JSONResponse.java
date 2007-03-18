/**
 * 
 */
package com.cookiecloaker.web.client;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

/**
 * TODO Add description JSONResponse (com.macroleads.web.client.json.JSONResponse)
 *
 * @author jon
 *
 */
public class JSONResponse {
	private JSONObject jsonObject = null;
	
	public static JSONResponse newInstance(String jsonString) {
		JSONResponse jsonResponse = new JSONResponse();
		jsonResponse.jsonObject = (JSONObject) JSONParser.parse(jsonString);
		return jsonResponse;
	}
	
	public com.google.gwt.json.client.JSONArray getErrors() {
		if (jsonObject.get("errors") != null)
			return jsonObject.get("errors").isArray();
		else
			return new JSONArray();
	}
	
	public boolean hasErrors() {
		return getErrors().size() > 0;
	}
	
	public JSONObject getResult() {
		if (jsonObject.get("result") != null)
			return (JSONObject) jsonObject.get("result");
		else
			return null;
	}
	
	public JSONObject getJSONObject() {
		return jsonObject;
	}
	
}
