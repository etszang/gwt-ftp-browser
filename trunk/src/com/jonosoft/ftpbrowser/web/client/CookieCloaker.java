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
public interface CookieCloaker extends Constants {
	public static final CookieCloaker DEFAULT_INSTANCE = (CookieCloaker) GWT.create(CookieCloaker.class);
	
	/**
	 * @gwt.key cookiecloaker.phpjson.ftpConnectionList.url
	 */
	String ftpConnectionListURL();
	
	/**
	 * @gwt.key cookiecloaker.phpjson.ftpConnectionSave.url
	 */
	String ftpConnectionSaveURL();
	
	/**
	 * @gwt.key cookiecloaker.phpjson.ftpPathList.url
	 */
	String ftpPathListURL();
	
	/**
	 * @gwt.key cookiecloaker.phpjson.ftpPathSave.url
	 */
	String ftpPathSaveURL();
	
	/**
	 * @gwt.key cookiecloaker.phpjson.ftpFileGroupList.url
	 */
	String ftpFileGroupListURL();
	
	/**
	 * @gwt.key cookiecloaker.phpjson.ftpFileGroupSave.url
	 */
	String ftpFileGroupSaveURL();
}
