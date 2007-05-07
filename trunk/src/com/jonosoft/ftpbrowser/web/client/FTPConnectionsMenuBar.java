/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MenuBar;
import com.jonosoft.ftpbrowser.web.client.FTPSiteMenuItem.FTPMenuItemCommandListener;

/**
 * @author Jkelling
 *
 */
public class FTPConnectionsMenuBar extends MenuBar implements LoadsRemoteData {
	
	private FTPTree ftpTree = null;
	private List ftpSites = null;
	private final FTPMenuItemCommandListener ftpMenuItemCommandListener = new FTPMenuItemCommandListener() {
		public void onFTPMenuItemConnect(FTPSite ftpSite) {
			if (ftpTree != null)
				ftpTree.setFTPConnection(ftpSite);
		}
		public void onFTPMenuItemsChanged() {
			reloadRemoteData();
		}
	};
	
	public FTPConnectionsMenuBar(FTPTree ftpTree) {
		super(true);
		
		this.ftpTree = ftpTree;
		
		setAutoOpen(true);
		addStyleName("ftpconnections-menubar");
		
		rebuildMenuBar();
	}
	
	public void reloadRemoteData() {
		reloadRemoteData(null);
	}
	
	public void reloadRemoteData(final AsyncCallback callback) {
		FTPService.Util.getInstance().getUserFTPSites(new AsyncCallback() {
			public void onFailure(Throwable caught) {
				if (callback != null)
					callback.onFailure(caught);
				Window.alert("Unexpected failure retrieving your saved FTP connections. If this continues, please contact our staff.\n\nMessage:\n" + caught.getMessage());
			}
			
			public void onSuccess(Object result) {
				clearItems();
				ftpSites = (List) result;
				for (Iterator it = ftpSites.iterator(); it.hasNext();) {
					final FTPSite ftpSite = (FTPSite) it.next();
					addItem(new FTPSiteMenuItem(ftpSite, ftpMenuItemCommandListener));
				}
				if (callback != null)
					callback.onSuccess(result);
			}
			
		});
	}
	
	public void clearItems() {
		super.clearItems();
		rebuildMenuBar();
	}
	
	public List getFtpSites() {
		return ftpSites;
	}
	
	private void rebuildMenuBar() {
		addItem(new FTPSiteMenuItem(null, ftpMenuItemCommandListener));
		addItem("<hr>", true, (Command)null);
	}

}
