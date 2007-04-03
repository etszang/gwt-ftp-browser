/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

/**
 * @author Jkelling
 *
 */
public class HTTPRequestUtil {
	
	public static void addKeyValuePair(StringBuffer sb, String key, String value) {
		if (sb.length() > 0)
			sb.append("&");
		sb.append(key + "=" + value);
	}
	
}
