/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.HTTPRequest;
import com.google.gwt.user.client.ResponseTextHandler;

/**
 * @author Jkelling
 *
 */
public class FTPConnection {
	private String server = null;
	private int port = 21;
	private String username = null;
	private String password = null;
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getServer() {
		return server;
	}
	
	public void setServer(String server) {
		this.server = server;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void getList(String path, FTPAsyncCallback callback) {
		String params = getFTPAndURLQueryParamsAsString(this, path, "dir", null);
		HTTPRequest.asyncGet(FTP.DEFAULT_INSTANCE.phpFtpJsonUrlBase()+params, new FTPResponseHandler(callback));
	}
	
	private static String getFTPAndURLQueryParamsAsString(FTPConnection conn, String path, String function, final String [] params) {
		List paramArray = new ArrayList();
		
		paramArray.add("server");	paramArray.add(conn.getServer());
		paramArray.add("port");		paramArray.add(Integer.toString(conn.getPort()));
		paramArray.add("username");	paramArray.add(conn.getUsername());
		paramArray.add("password");	paramArray.add(conn.getPassword());
		paramArray.add("dir");		paramArray.add(path);
		paramArray.add("function");	paramArray.add(function);
		
		if (params != null)
			for (int i = 0; i < params.length; i++)
				paramArray.add(params[i]);
		
		return getURLQueryParamsAsString(paramArray);
	}
	
	private static String getURLQueryParamsAsString(final List paramArray) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < paramArray.size(); i+=2) {
			if (sb.length() > 0)
				sb.append("&");
			sb.append(paramArray.get(i) + "=" + paramArray.get(i+1));
		}
		return sb.toString();
	}
	
	private static List getListFromJSONArrayOfJSONStrings(JSONArray jsonArray) {
		final List ar = new ArrayList();
		for (int i = 0; i < jsonArray.size(); i++) {
			ar.add(((JSONString) jsonArray.get(i)).stringValue());
		}
		return ar;
	}
	
	private static class FTPResponseHandler implements ResponseTextHandler {
		private FTPAsyncCallback callback = null;
		
		public FTPResponseHandler(FTPAsyncCallback callback) {
			this.callback = callback;
		}
		
		public void onCompletion(String responseText) {
			JSONResponse jsonResponse = JSONResponse.newInstance(responseText);
			JSONArray dirs = (JSONArray) jsonResponse.getResult().get("dirs");
			JSONArray files = (JSONArray) jsonResponse.getResult().get("files");
			if (this.callback != null){
				this.callback.onSuccess(getListFromJSONArrayOfJSONStrings(dirs));
				this.callback.onSuccess(getListFromJSONArrayOfJSONStrings(files));
			}
		}
	}
	

}
