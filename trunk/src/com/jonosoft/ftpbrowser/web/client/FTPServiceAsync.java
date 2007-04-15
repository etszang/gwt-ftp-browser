package com.jonosoft.ftpbrowser.web.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FTPServiceAsync {
	
	
	/**
	 * @param site
	 * @param path
	 * @return List of {@link FTPFileItem}s
	 * @throws FTPIOException 
	 */
	public void getFileList(FTPSite site, String path, AsyncCallback callback);
	
	
	/**
	 * @param userId
	 * @return List {@link FTPSite}s
	 */
	public void getUserFTPSites(int userId, AsyncCallback callback);
	
	
	/**
	 * @param site
	 * @return
	 * @throws FTPBrowserFatalException
	 */
	public void saveUserFTPSite(FTPSite site, AsyncCallback callback);
	
	
	/**
	 * @param site
	 * @throws FTPBrowserFatalException
	 */
	public void deleteUserFTPSite(FTPSite site, AsyncCallback callback);
}
