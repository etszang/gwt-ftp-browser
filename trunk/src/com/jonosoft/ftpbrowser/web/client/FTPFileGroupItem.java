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
	private Integer ftpSite = null;
	
	
	public Integer getFtpFileGroupId() {
		return ftpFileGroupId;
	}

	public Integer getFtpFileGroupItemId() {
		return ftpFileGroupItemId;
	}

	public Integer getFtpSite() {
		return ftpSite;
	}

	public void setFtpFileGroupId(Integer ftpFileGroupId) {
		this.ftpFileGroupId = ftpFileGroupId;
	}

	public void setFtpFileGroupItemId(Integer ftpFileGroupItemId) {
		this.ftpFileGroupItemId = ftpFileGroupItemId;
	}

	public void setFtpSite(Integer ftpSiteId) {
		this.ftpSite= ftpSiteId;
	}
	
	

}
