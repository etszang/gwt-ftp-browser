package com.jonosoft.ftpbrowser.web.client;

import java.util.List;
import java.util.Set;

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
	 * @return List {@link FTPSite}s
	 */
	public void getUserFTPSites(AsyncCallback callback);
	
	
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
	
	
	/**
	 * @param groupId
	 * @return
	 * @throws FTPBrowserFatalException
	 */
	public void getUserFTPFileItems(Integer groupId, AsyncCallback callback);
	
	
	/**
	 * @param groupId
	 * @param ftpFileItems
	 * @throws FTPBrowserFatalException
	 */
	public void saveUserFTPFileItems(Integer groupId, List ftpFileItems, AsyncCallback callback);
}
