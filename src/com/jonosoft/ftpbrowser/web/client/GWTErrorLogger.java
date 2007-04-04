/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

/**
 * @author Jkelling
 *
 */
public class GWTErrorLogger {
	
	public static void logError(Throwable e) {
		final JSRequestBuilder requestBuilder = new JSRequestBuilder(RequestBuilder.POST, GWT.getModuleBaseURL()+CookieCloaker.DEFAULT_INSTANCE.gwtErrorSaveURL());
		
		requestBuilder.addParameter("stack_trace", e.getMessage());
		
		DeferredCommand.add(new Command() {
			public void execute() {
				try {
					requestBuilder.sendRequest(new RequestCallback() {
						public void onError(Request request, Throwable exception) {
						}
						
						public void onResponseReceived(Request request, Response response) {
						}
					});
				} catch (RequestException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
}
