/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

/**
 * @author Jkelling
 *
 */
public interface FTPConnectionSettingsListener {
	
	public void onFTPConnectionSettingsSave(FTPSite result);
	
	public void onFTPConnectionSettingsCancel();
	
}
