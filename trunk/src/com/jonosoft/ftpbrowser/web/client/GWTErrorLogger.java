/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Jkelling
 *
 */
public class GWTErrorLogger {
	
	public static void logError(Throwable e) {
		GWTLoggerService.Util.getInstance().error("", e, new AsyncCallback() {

			public void onFailure(Throwable caught) {
				System.out.println(caught.toString());
			}

			public void onSuccess(Object result) {
			}
			
		});
		
		/*final JSRequestBuilder requestBuilder = new JSRequestBuilder(RequestBuilder.POST, GWT.getModuleBaseURL()+CookieCloaker.DEFAULT_INSTANCE.gwtErrorSaveURL());
		
		requestBuilder.addParameter("stack_trace", e.toString());
		
		try {
			requestBuilder.sendRequest(new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					GWT.log("Error logging remotely: " + exception.toString(), exception);
				}
				
				public void onResponseReceived(Request request, Response response) {
					System.out.println("Successfully logged error remotely...");
					System.out.println("Response: " + response.getText());
				}
			});
		} catch (RequestException e1) {
			e1.printStackTrace();
		}*/
	}
	
}
