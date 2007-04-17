package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GWTLoggerServiceAsync {
	
	
	public void debug(String message, AsyncCallback callback);
	
	public void debug(String message, Throwable de, AsyncCallback callback);
	
	public void error(String message, AsyncCallback callback);
	
	public void error(String message, Throwable ee, AsyncCallback callback);
}
