/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

/**
 * @author Jkelling
 * 
 */
public class FTPFileGroupItem extends FTPFileItem {
	
	private Integer ftpFileGroupId = null;
	private Integer ftpFileGroupItemId = null;
	
	public Integer getFtpFileGroupId() {
		return ftpFileGroupId;
	}

	public Integer getFtpFileGroupItemId() {
		return ftpFileGroupItemId;
	}

	public void setFtpFileGroupId(Integer ftpFileGroupId) {
		this.ftpFileGroupId = ftpFileGroupId;
	}

	public void setFtpFileGroupItemId(Integer ftpFileGroupItemId) {
		this.ftpFileGroupItemId = ftpFileGroupItemId;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("FTPFileGroupItemId: " + ftpFileGroupItemId + "\n");
		sb.append("FTPFileGroupId: " + ftpFileGroupId + "\n");
		
		return sb.toString() + super.toString();
	}

}
