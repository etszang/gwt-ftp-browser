/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Jkelling
 * 
 */
public class FTPFileGroup implements IsSerializable {

	private int groupId = 0;
	private int userId = 0;
	private String name = null;
	
	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
