/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * @author Jkelling
 *
 */
public interface LoadsRemoteData {
	
	public void reloadRemoteData();
	
	public void reloadRemoteData(AsyncCallback callback);
	
}
