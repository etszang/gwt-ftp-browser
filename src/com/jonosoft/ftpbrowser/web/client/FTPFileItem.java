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
	
	private FTPConnection ftpConnection = null;
	private String name = null;
	private String type = null; // "f" = file; "d" = directory
	private String fullPath = null;
	
	public FTPFileItem(FTPConnection ftpConnection) {
		this.ftpConnection = ftpConnection;
	}
	
	public FTPFileItem(FTPConnection ftpConnection, String name, String type) {
		this(ftpConnection);
		setName(name);
		setType(type);
		setFullPath("/" + name);
	}
	
	public FTPFileItem(FTPConnection ftpConnection, String name, String type, String fullPath) {
		this(ftpConnection, name, type);
		setFullPath(getFullPath(fullPath, name));
	}
	
	public FTPFileItem(FTPConnection ftpConnection, String name, String type, FTPFileItem parent) {
		this(ftpConnection, name, type, getFullPath(parent.getFullPath(), name));
	}
	
	private static String getFullPath(String path, String name) {
		return path.replaceFirst("[/]+$", "") + "/" + name;
	}
	
	public Object clone() {
		return new FTPFileItem(getFTPConnection(), getName(), getType(), getFullPath());
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
	
	public String getFullPath() {
		return fullPath;
	}
	
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}
	
	public FTPConnection getFTPConnection() {
		return ftpConnection;
	}
	
	public void setFTPConnection(FTPConnection ftpConnection) {
		this.ftpConnection = ftpConnection;
	}
	
}
