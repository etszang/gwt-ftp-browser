package com.jonosoft.ftpbrowser.web.client;

import java.util.List;
import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.HTTPRequest;
import com.google.gwt.user.client.ResponseTextHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class FTPFileGroupController {/*
	
	public static void getFTPFileGroupListForActiveUser(AsyncCallback callback) {
		HTTPRequest.asyncGet(GWT.getModuleBaseURL()+CookieCloaker.DEFAULT_INSTANCE.ftpFileGroupListURL(), new GroupListResponseTextHandler(callback));
	}
	
	private static class GroupListResponseTextHandler implements ResponseTextHandler {
		private AsyncCallback callback = null;
		
		public GroupListResponseTextHandler(AsyncCallback callback) {
			this.callback = callback;
		}
		
		public void onCompletion(String responseText) {
			if (callback == null)
				return;
			
			JSONResponse jsonResponse = JSONResponse.newInstance(responseText);
			
			try {
				jsonResponse.handleErrors(); // throw any errors that occur
				
				final JSONArray jsonArray = (JSONArray) jsonResponse.getResult().get("groupList");
				final List list = new Vector();
				
				for (int i = 0; i < jsonArray.size(); i++) {
					list.add(FTPFileGroup.getInstance((JSONObject) jsonArray.get(i)));
				}
				
				callback.onSuccess(list);
			}
			catch (Throwable caught) {
				GWTErrorLogger.logError(caught);
				callback.onFailure(caught);
			}
		}
	}

*/}
