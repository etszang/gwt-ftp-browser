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
}
