/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ResponseTextHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Jkelling
 *
 */
public class FTPConnectionController {
	
	public static void getList(FTPSite site, final String path, final FTPAsyncCallback callback) {
		FTPService.Util.getInstance().getFileList(site, path, new AsyncCallback() {
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			public void onSuccess(Object result) {
				List list = (List) result;
				if (callback != null){
					for (Iterator it = list.iterator(); it.hasNext();)
						if (((FTPFileItem) it.next()).getName().matches("^[\\.]{1,2}$"))
							it.remove();
					Collections.sort(list);
					callback.onSuccess(list);
				}
			}
		});
	}
	
	private static String getFTPAndURLQueryParamsAsString(FTPSite site, String path, String function, final String [] params) {
		List paramArray = new ArrayList();
		
		paramArray.add("server");	paramArray.add(site.getHost());
		paramArray.add("port");		paramArray.add(site.getPort().toString());
		paramArray.add("username");	paramArray.add(site.getUsername());
		paramArray.add("password");	paramArray.add(site.getPassword());
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
	
	private static List g(FTPSite site, String path, JSONArray jsonArray, String type) {
		final List ar = new ArrayList();
		String name = null;
		for (int i = 0; i < jsonArray.size(); i++) {
			name = ((JSONString) jsonArray.get(i)).stringValue();
			ar.add(new FTPFileItem(site, name, type, path));
		}
		return ar;
	}
	
	private static class FTPResponseHandler implements ResponseTextHandler {
		private FTPSite ftpSite = null;
		private FTPAsyncCallback callback = null;
		private String path = null;
		
		public FTPResponseHandler(FTPSite ftpSite, String path, FTPAsyncCallback callback) {
			this.ftpSite = ftpSite;
			this.path = path;
			this.callback = callback;
		}
		
		public void onCompletion(String responseText) {/*
			JSONResponse jsonResponse = JSONResponse.newInstance(responseText);
			JSONArray dirs = (JSONArray) jsonResponse.getResult().get("dirs");
			JSONArray files = (JSONArray) jsonResponse.getResult().get("files");
			if (this.callback != null){
				List items = getListFromJSONArrayOfJSONStrings(ftpSite, path, dirs, "d");
				items.addAll(g(ftpSite, path, files, "f"));
				for (Iterator it = items.iterator(); it.hasNext();)
					if (((FTPFileItem) it.next()).getName().matches("^[\\.]{1,2}$"))
						it.remove();
				this.callback.onSuccess(items);
			}
		*/}
	}
	

}
