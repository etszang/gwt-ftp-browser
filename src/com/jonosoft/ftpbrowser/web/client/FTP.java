/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;

/**
 * @author Jkelling
 * 
 */
public interface FTP extends Constants {
	public static final FTP DEFAULT_INSTANCE = (FTP) GWT.create(FTP.class);
	
	/**
	 * @gwt.key ftp.factory
	 */
	String connectionFactory();
	
	/**
	 * @gwt.key ftp.phpftpjson.urlbase
	 */
	String phpFtpJsonUrlBase();
}
