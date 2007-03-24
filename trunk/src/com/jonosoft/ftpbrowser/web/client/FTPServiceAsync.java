/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Jkelling
 *
 */
public interface FTPServiceAsync {

	public void getDirectoryContents(FTPConnection conn, String path, AsyncCallback callback);

}
