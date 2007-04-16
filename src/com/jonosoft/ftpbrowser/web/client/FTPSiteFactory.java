/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * @author Jkelling
 *
 */
public class FTPSiteFactory {
	
	public static FTPSite getInstance(JSONObject jsonObject) {
		FTPSite conn = new FTPSite();
		
		conn.setFtpSiteId(new Integer(((JSONString) jsonObject.get("ftp_site_id")).stringValue()));
		conn.setUserId(new Integer(((JSONString) jsonObject.get("user_id")).stringValue()));
		conn.setHost(((JSONString) jsonObject.get("host")).stringValue());
		conn.setPort(new Integer(((JSONString) jsonObject.get("port")).stringValue()));
		conn.setUsername(((JSONString) jsonObject.get("username")).stringValue());
		conn.setPassword(((JSONString) jsonObject.get("password")).stringValue());

		return conn;
	}
	
}
