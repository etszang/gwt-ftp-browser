/**
 * 
 */
package com.jonosoft.ftpbrowser.web.server;

import com.jonosoft.ftpbrowser.web.client.FTPBrowserFatalException;

/**
 * @author Jkelling
 *
 */
public class FTPBrowserDBConnection extends DBConnection {
	
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql:///cookiecloaker";
	private static final String DB_USER = "admin";
	private static final String DB_PASS = "VGpuyk3t";
	
	public FTPBrowserDBConnection() throws FTPBrowserFatalException {
		super(DB_DRIVER, DB_URL, DB_USER, DB_PASS);
	}
	
}
