/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

/**
 * TODO Add description JSONResponse (com.macroleads.web.client.json.JSONResponse)
 *
 * @author jon
 *
 */
public class JSONResponse {
	private JSONObject jsonObject = null;
	
	public static JSONResponse newInstance(String responseText) {
		try {
			JSONResponse jsonResponse = new JSONResponse();
			jsonResponse.jsonObject = (JSONObject) JSONParser.parse(responseText);
			return jsonResponse;
		}
		catch (Throwable e) {
			throw new RuntimeException("JSONResponse couldn't parse text: " + responseText, e);
		}
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
	
	/**
	 * Throws a RuntimeException if this JSONResponse has any errors.
	 * 
	 * TODO Create custom Exception
	 */
	public void handleErrors() {
		if (hasErrors())
			throw new RuntimeException(((JSONString) getErrors().get(0)).stringValue());
	}
	
}
