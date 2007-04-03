/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.HTTPRequest;
import com.google.gwt.user.client.ResponseTextHandler;

/**
 * @author Jkelling
 *
 */
public class FTPConnection {
	private int ftpSiteId;
	private String server = null;
	private int port = 21;
	private String username = null;
	private String password = null;
	
	public static FTPConnection getInstance(JSONObject jsonObject) {
		FTPConnection conn = new FTPConnection();
		
		conn.setFtpSiteId(Integer.parseInt(((JSONString) jsonObject.get("ftp_site_id")).stringValue()));
		conn.setServer(((JSONString) jsonObject.get("host")).stringValue());
		conn.setPort(Integer.parseInt(((JSONString) jsonObject.get("port")).stringValue()));
		conn.setUsername(((JSONString) jsonObject.get("username")).stringValue());
		conn.setPassword(((JSONString) jsonObject.get("password")).stringValue());

		return conn;
	}
	
	public boolean equals(Object arg0) {
		if (arg0 instanceof FTPConnection) {
			FTPConnection ftpConnection = (FTPConnection) arg0;
			return getFtpSiteId() == ftpConnection.getFtpSiteId();
		}
		return false;
	}
	
	public int getFtpSiteId() {
		return ftpSiteId;
	}
	
	public void setFtpSiteId(int ftpSiteId) {
		this.ftpSiteId = ftpSiteId;
	}
	
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
		System.out.println("Requesting: " + path);
		HTTPRequest.asyncGet(GWT.getModuleBaseURL()+FTP.DEFAULT_INSTANCE.phpFtpJsonUrlBase()+params, new FTPResponseHandler(this, path, callback));
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
	
	private static List getListFromJSONArrayOfJSONStrings(FTPConnection ftpConnection, String path, JSONArray jsonArray, String type) {
		final List ar = new ArrayList();
		String name = null;
		for (int i = 0; i < jsonArray.size(); i++) {
			name = ((JSONString) jsonArray.get(i)).stringValue();
			ar.add(new FTPFileItem(ftpConnection, name, type, path));
		}
		return ar;
	}
	
	private static class FTPResponseHandler implements ResponseTextHandler {
		private FTPConnection ftpConnection = null;
		private FTPAsyncCallback callback = null;
		private String path = null;
		
		public FTPResponseHandler(FTPConnection ftpConnection, String path, FTPAsyncCallback callback) {
			this.ftpConnection = ftpConnection;
			this.path = path;
			this.callback = callback;
		}
		
		public void onCompletion(String responseText) {
			JSONResponse jsonResponse = JSONResponse.newInstance(responseText);
			JSONArray dirs = (JSONArray) jsonResponse.getResult().get("dirs");
			JSONArray files = (JSONArray) jsonResponse.getResult().get("files");
			if (this.callback != null){
				List items = getListFromJSONArrayOfJSONStrings(ftpConnection, path, dirs, "d");
				items.addAll(getListFromJSONArrayOfJSONStrings(ftpConnection, path, files, "f"));
				for (Iterator it = items.iterator(); it.hasNext();)
					if (((FTPFileItem) it.next()).getName().matches("^[\\.]{1,2}$"))
						it.remove();
				this.callback.onSuccess(items);
			}
		}
	}
	

}
