package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface GWTLoggerService extends RemoteService {
	
	
	public void debug(String message);
	
	public void debug(String message, Throwable de);
	
	public void error(String message);
	
	public void error(String message, Throwable ee);
	
	
	/**
	 * Utility class for simplifing access to the instance of async service.
	 */
	public static class Util {
		private static GWTLoggerServiceAsync instance;
		public static GWTLoggerServiceAsync getInstance(){
			if (instance == null) {
				instance = (GWTLoggerServiceAsync) GWT.create(GWTLoggerService.class);
				ServiceDefTarget target = (ServiceDefTarget) instance;
				target.setServiceEntryPoint(GWT.getModuleBaseURL() + "/GWTLoggerService");
			}
			return instance;
		}
	}
}
