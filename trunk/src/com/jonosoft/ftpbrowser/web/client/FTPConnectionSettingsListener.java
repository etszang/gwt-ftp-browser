/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

/**
 * @author Jkelling
 *
 */
public interface FTPConnectionSettingsListener {
	
	public void onFTPConnectionSettingsSave(FTPConnection result);
	
	public void onFTPConnectionSettingsCancel();
	
}
