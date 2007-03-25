/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Jkelling
 *
 */
public class FTPFileItem implements IsSerializable {
	
	private String name = null;
	private String type = null; // "f" = file; "d" = directory
	
	public FTPFileItem() {
	}
	
	public FTPFileItem(String name, String type) {
		setName(name);
		setType(type);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
}
