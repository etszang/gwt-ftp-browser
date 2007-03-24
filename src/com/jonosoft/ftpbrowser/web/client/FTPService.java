/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * @author Jkelling
 *
 */
public interface FTPService extends RemoteService {

	public static final String SERVICE_URI = "/FTPService";
	
	public List getDirectoryContents(FTPConnection conn, String path) throws FTPIOException;

	public static class Util {

		public static FTPServiceAsync getInstance() {

			FTPServiceAsync instance = (FTPServiceAsync) GWT
					.create(FTPService.class);
			ServiceDefTarget target = (ServiceDefTarget) instance;
			target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
			return instance;
		}
	}

}
