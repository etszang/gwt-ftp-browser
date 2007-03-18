/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

/**
 * @author Jkelling
 *
 */
public interface FTPAsyncCallback {
	
	public void onSuccess(Object result);
	public void onFailure(Throwable caught);
	
}
