package com.jonosoft.ftpbrowser.web.server;

import com.jonosoft.ftpbrowser.web.client.GWTLoggerService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GWTLoggerServiceImpl extends RemoteServiceServlet implements GWTLoggerService {
	
	public void debug(String message) {
		System.out.println(message);
	}
	
	public void debug(String message, Throwable de) {
		System.out.println(message);
		System.out.println(de.toString());
	}

	public void error(String message) {
		System.out.println(message);
	}
	
	public void error(String message, Throwable ee) {
		System.out.println(message);
		System.out.println(ee.toString());
	}
}
