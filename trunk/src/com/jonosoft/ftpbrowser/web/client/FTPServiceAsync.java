/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Jkelling
 *
 */
public interface FTPServiceAsync {

	public void getDirectoryContents(FTPConnection conn, String path, AsyncCallback callback);

}
