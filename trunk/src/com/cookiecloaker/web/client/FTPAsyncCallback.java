/**
 * 
 */
package com.cookiecloaker.web.client;

/**
 * @author Jkelling
 *
 */
public interface FTPAsyncCallback {
	
	public void onSuccess(Object result);
	public void onFailure(Throwable caught);
	
}
