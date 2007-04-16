/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

/**
 * @author Jkelling
 * 
 */
public class FTPFileGroupItem extends FTPFileItem {

	private FTPFileGroup ftpFileGroup = null;
	private Integer ftpFileGroupId = null;
	private Integer ftpFileGroupItemId = null;
	private Integer ftpSiteId = null;

	public FTPFileGroup getFtpFileGroup() {
		return ftpFileGroup;
	}

	public Integer getFtpFileGroupId() {
		return ftpFileGroupId;
	}

	public Integer getFtpFileGroupItemId() {
		return ftpFileGroupItemId;
	}

	public Integer getFtpSiteId() {
		return ftpSiteId;
	}

	public void setFtpFileGroup(FTPFileGroup ftpFileGroup) {
		this.ftpFileGroup = ftpFileGroup;
	}

	public void setFtpFileGroupId(Integer ftpFileGroupId) {
		this.ftpFileGroupId = ftpFileGroupId;
	}

	public void setFtpFileGroupItemId(Integer ftpFileGroupItemId) {
		this.ftpFileGroupItemId = ftpFileGroupItemId;
	}

	public void setFtpSiteId(Integer ftpSiteId) {
		this.ftpSiteId = ftpSiteId;
	}
	
	

}
