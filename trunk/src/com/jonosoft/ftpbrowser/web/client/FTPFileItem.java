/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Jkelling
 *
 */
public class FTPFileItem implements Comparable, IsSerializable {
	
	private Integer ftpSiteId = null;
	private String name = null;
	private String type = null; // "f" = file; "d" = directory
	private String fullPath = null;
	
	public FTPFileItem() {
	}
	
	public FTPFileItem(Integer ftpConnection) {
		this.ftpSiteId = ftpConnection;
	}
	
	public FTPFileItem(Integer ftpConnection, String name, String type) {
		this(ftpConnection, name, type, name);
	}
	
	public FTPFileItem(Integer ftpConnection, String name, String type, String fullPath) {
		this(ftpConnection);
		this.name = name;
		this.type = type;
		this.fullPath = getFullPath(fullPath, name);
	}
	
	public FTPFileItem(Integer ftpConnection, String name, String type, FTPFileItem parent) {
		this(ftpConnection, name, type, getFullPath(parent.getFullPath(), name));
	}
	
	private static String getFullPath(String path, String name) {
		return (path.replaceFirst("[/]+$", "") + "/" + name).replaceAll(
				"[/]\\.([/]|$)", "/").replaceAll(
				"[/]([^\\./]|[^/][^\\./][^/]*?)[/]..([/]|$)", "/");
	}
	
	public Object clone() {
		FTPFileItem ftpFileItem = new FTPFileItem(getFtpSiteId());
		ftpFileItem.name = getName();
		ftpFileItem.type = getType();
		ftpFileItem.fullPath = getFullPath();
		return ftpFileItem;
	}
	
	public boolean equals(Object arg0) {
		if (arg0 instanceof FTPFileItem) {
			FTPFileItem ftpFileItem = (FTPFileItem) arg0;
			boolean ftpConnectionsAreEqual = false;
			boolean pathsAreEqual = false;
			if (getFtpSiteId() == null)
				ftpConnectionsAreEqual = (ftpFileItem.getFtpSiteId() == null);
			else
				ftpConnectionsAreEqual = (getFtpSiteId().equals(ftpFileItem.getFtpSiteId()));
			if (getFullPath() == null)
				pathsAreEqual = (ftpFileItem.getFullPath() == null);
			else
				pathsAreEqual = (getFullPath().equals(ftpFileItem.getFullPath()));
			return ftpConnectionsAreEqual && pathsAreEqual;
		}
		return false;
	}
	
	public int hashCode() {
		StringBuffer sb = new StringBuffer();
		if (getFtpSiteId() != null)
			sb.append(getFtpSiteId()+":");
		else
			sb.append("null:");
		if (getFullPath() == null)
			sb.append("null");
		else
			// Will always begin with "/"
			sb.append(getFullPath());
		return sb.toString().hashCode();
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
		if (fullPath != null)
			setName(fullPath.replaceFirst("([^/]+$)", "$1"));
	}
	
	/* Currently, this can cause inconsistencies
	 * public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}*/
	
	public Integer getFtpSiteId() {
		return ftpSiteId;
	}
	
	public void setFtpSiteId(Integer ftpConnection) {
		this.ftpSiteId = ftpConnection;
	}
	
	public final boolean isFile() {
		return "f".equalsIgnoreCase(type);
	}
	
	public final boolean isDirectory() {
		return "d".equalsIgnoreCase(type);
	}

	/**
	 * @see java.lang.Comparable#compareTo(T)
	 */
	public int compareTo(Object arg0) {
		if (arg0 instanceof FTPFileItem) {
			return compareTo((FTPFileItem) arg0);
		}
		return 0;
	}
	
	public int compareTo(FTPFileItem ftpFileItem) {
		int compare = getType().compareTo(ftpFileItem.getType());
		if (compare != 0)
			return compare;
		else
			return getFullPath().compareTo(ftpFileItem.getFullPath());
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("FTPSiteId: " + ftpSiteId + "\n");
		sb.append("Full Path: " + fullPath + "\n");
		sb.append("Name: " + name + "\n");
		sb.append("Type: " + type + "\n");
		
		return sb.toString();
	}
	
}
