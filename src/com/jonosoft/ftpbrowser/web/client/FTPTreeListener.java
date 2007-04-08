/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.user.client.ui.TreeListener;

/**
 * @author Jkelling
 *
 */
public interface FTPTreeListener extends TreeListener {
	
	public void afterDataReceived(FTPTreeItem parentTreeItem);
	
}
