/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Jkelling
 * 
 */
public class FTPSite implements IsSerializable {
	private Integer ftpSiteId;
	private String host;
	private String password;
	private Integer port;
	private Integer userId;
	private String username;
	
	public boolean equals(Object arg0) {
		if (arg0 instanceof FTPSite) {
			FTPSite ftpSite = (FTPSite) arg0;
			return getFtpSiteId() == ftpSite.getFtpSiteId();
		}
		return false;
	}

	public Integer getFtpSiteId() {
		return ftpSiteId;
	}

	public String getHost() {
		return host;
	}

	public String getPassword() {
		return password;
	}

	public Integer getPort() {
		return port;
	}

	public Integer getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public void setFtpSiteId(Integer ftpSiteId) {
		this.ftpSiteId = ftpSiteId;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setPort(Integer port) {
		this.port = port;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String toString() {
		final String LS = String.valueOf((char)13);
		final StringBuffer sb = new StringBuffer();
		
		sb.append("Host: " + host + LS);
		sb.append("Port: " + port + LS);
		sb.append("Username: " + username + LS);
		sb.append("Password: " + password);
		
		return sb.toString();
	}
}
