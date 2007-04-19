/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Jkelling
 * 
 */
public class FTPFileGroup implements IsSerializable {

	private Integer ftpFileGroupId = new Integer(0);
	private String name = null;
	private Integer userId = new Integer(0);
	
	public FTPFileGroup() {
	}

	public Integer getFtpFileGroupId() {
		return ftpFileGroupId;
	}
	
	public String getName() {
		return name;
	}

	public Integer getUserId() {
		return userId;
	}
	
	public void setFtpFileGroupId(Integer groupId) {
		this.ftpFileGroupId = groupId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
