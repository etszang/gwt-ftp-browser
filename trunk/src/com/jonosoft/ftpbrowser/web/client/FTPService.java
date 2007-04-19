package com.jonosoft.ftpbrowser.web.client;

import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface FTPService extends RemoteService {
	
	
	/**
	 * @param site
	 * @param path
	 * @return List of {@link FTPFileItem}s
	 * @throws FTPIOException 
	 * @gwt.typeArgs <com.jonosoft.ftpbrowser.web.client.FTPFileItem>
	 */
	public List getFileList(FTPSite site, String path) throws FTPIOException;
	
	
	/**
	 * @return List {@link FTPSite}s
	 * @gwt.typeArgs <com.jonosoft.ftpbrowser.web.client.FTPSite>
	 */
	public List getUserFTPSites() throws FTPBrowserFatalException;
	
	
	/**
	 * @param site
	 * @return
	 * @throws FTPBrowserFatalException
	 */
	public FTPSite saveUserFTPSite(FTPSite site) throws FTPBrowserFatalException;
	
	
	/**
	 * @param site
	 * @throws FTPBrowserFatalException
	 */
	public void deleteUserFTPSite(FTPSite site) throws FTPBrowserFatalException;
	
	
	/**
	 * @param groupId
	 * @return
	 * @throws FTPBrowserFatalException
	 * @gwt.typeArgs <com.jonosoft.ftpbrowser.web.client.FTPFileItem>
	 */
	public List getUserFTPFileItems(Integer groupId) throws FTPBrowserFatalException;
	
	
	/**
	 * @param groupId
	 * @param ftpFileItems
	 * @throws FTPBrowserFatalException
	 * @gwt.typeArgs ftpFileItems<com.jonosoft.ftpbrowser.web.client.FTPFileItem>
	 */
	public void saveUserFTPFileItems(Integer groupId, List ftpFileItems) throws FTPBrowserFatalException;
	
	
	/**
	 * Utility class for simplifing access to the instance of async service.
	 */
	public static class Util {
		private static FTPServiceAsync instance;
		public static FTPServiceAsync getInstance(){
			if (instance == null) {
				instance = (FTPServiceAsync) GWT.create(FTPService.class);
				ServiceDefTarget target = (ServiceDefTarget) instance;
				target.setServiceEntryPoint(GWT.getModuleBaseURL() + "/FTPService");
			}
			return instance;
		}
	}
}
