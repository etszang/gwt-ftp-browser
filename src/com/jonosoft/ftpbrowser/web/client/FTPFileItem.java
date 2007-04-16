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
public class FTPFileItem implements Comparable, IsSerializable {
	
	private FTPSite ftpSite = null;
	private String name = null;
	private String type = null; // "f" = file; "d" = directory
	private String fullPath = null;
	
	public static FTPFileItem getInstance(JSONObject json) {
		FTPFileItem ftpFileItem = new FTPFileItem(new FTPSite());
		
		ftpFileItem.ftpSite.setFtpSiteId(new Integer(((JSONString) json.get("ftp_site_id")).stringValue()));
		ftpFileItem.name = ((JSONString) json.get("name")).stringValue();
		ftpFileItem.type = ((JSONString) json.get("type")).stringValue();
		ftpFileItem.fullPath = ((JSONString) json.get("path")).stringValue();
		
		return ftpFileItem;
	}
	
	public FTPFileItem() {
	}
	
	public FTPFileItem(FTPSite ftpConnection) {
		this.ftpSite = ftpConnection;
	}
	
	public FTPFileItem(FTPSite ftpConnection, String name, String type) {
		this(ftpConnection, name, type, name);
	}
	
	public FTPFileItem(FTPSite ftpConnection, String name, String type, String fullPath) {
		this(ftpConnection);
		this.name = name;
		this.type = type;
		this.fullPath = getFullPath(fullPath, name);
	}
	
	public FTPFileItem(FTPSite ftpConnection, String name, String type, FTPFileItem parent) {
		this(ftpConnection, name, type, getFullPath(parent.getFullPath(), name));
	}
	
	private static String getFullPath(String path, String name) {
		return path.replaceFirst("[/]+$", "") + "/" + name;
	}
	
	public Object clone() {
		FTPFileItem ftpFileItem = new FTPFileItem(getFTPConnection());
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
			if (getFTPConnection() == null)
				ftpConnectionsAreEqual = (ftpFileItem.getFTPConnection() == null);
			else
				ftpConnectionsAreEqual = (getFTPConnection().equals(ftpFileItem.getFTPConnection()));
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
		if (getFTPConnection() != null)
			sb.append(getFTPConnection().getFtpSiteId()+":");
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
	
	/* Currently, this can cause inconsistencies
	 * public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}*/
	
	public FTPSite getFTPConnection() {
		return ftpSite;
	}
	
	public void setFTPConnection(FTPSite ftpConnection) {
		this.ftpSite = ftpConnection;
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
	
}
