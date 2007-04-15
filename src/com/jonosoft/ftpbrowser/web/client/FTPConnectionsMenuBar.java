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
import com.google.gwt.user.client.ui.MenuItem;

/**
 * @author Jkelling
 *
 */
public class FTPConnectionsMenuBar extends MenuBar implements FTPConnectionSettingsListener   {
	
	private final FTPConnectionSettingsPopupPanel myFtp = new FTPConnectionSettingsPopupPanel(true);
	private FTPTree ftpTree = null;
	
	public FTPConnectionsMenuBar(FTPTree ftpTree) {
		super(true);
		
		this.ftpTree = ftpTree;
		
		setAutoOpen(true);
		addStyleName("ftpconnections-menubar");
		
		myFtp.addFTPConnectionSettingsListener(this);
		
		refreshListFromServer();
	}
	
	public void onFTPConnectionSettingsSave(FTPSite result) {
		FTPService.Util.getInstance().saveUserFTPSite(result, new AsyncCallback() {
			public void onFailure(Throwable caught) {
				Window.alert("Unexpected failure saving the FTP Connection. If this continues, please contact our staff.\n\nMessage:\n" + caught.getMessage());
				refreshListFromServer();
			}

			public void onSuccess(Object result) {
				refreshListFromServer();
				Window.alert("FTP site has been saved.\n\n" + result.toString());
			}
		});
		
		myFtp.hide();
	}
	
	public void onFTPConnectionSettingsCancel() {
		myFtp.hide();
	}
	
	private void refreshListFromServer() {
		FTPService.Util.getInstance().getUserFTPSites(7, new AsyncCallback() {
			public void onFailure(Throwable caught) {
				Window.alert("Unexpected failure retrieving your saved FTP connections. If this continues, please contact our staff.\n\nMessage:\n" + caught.getMessage());
			}
			
			public void onSuccess(Object result) {
				clearItems();
				List ftpSites = (List) result;
				for (Iterator it = ftpSites.iterator(); it.hasNext();)
					addItem(new FTPConnectionMenuItem((FTPSite) it.next()));
			}
			
		});
	}
	
	public void clearItems() {
		super.clearItems();
		rebuildMenuBar();
	}
	
	private void rebuildMenuBar() {
		MenuItem newFTPSiteMenuItem = new MenuItem("New FTP Site...", new Command() {
			public void execute() {
				FTPSite newFTPSite = new FTPSite();
				
				// XXX All of the userId stuff should be server-side, I don't know what I was thinking...
				newFTPSite.setUserId(7);
				
				// The default port for FTP is 21
				newFTPSite.setPort(21);
				
				myFtp.show();
				myFtp.setFTPSite(newFTPSite);
			}
		});
		
		addItem(newFTPSiteMenuItem);
		addItem("<hr>", true, (Command)null);
	}
	
	private class FTPConnectionMenuItem extends MenuItem {
		public FTPConnectionMenuItem(FTPSite ftpConnection) {
			super(ftpConnection.getHost(), true, new FTPConnectionSubMenu(ftpConnection));
			addStyleName("ftpconnections-menuitem");
		}
	}
	
	private class FTPConnectionSubMenu extends MenuBar {
		public FTPConnectionSubMenu(final FTPSite ftpSite) {
			super(true);
			addStyleName("ftpconnections-submenubar");
			
			addItem("Connect", new Command() {
				public void execute() {
					if (ftpTree != null)
						ftpTree.setFTPConnection(ftpSite);
				}
			});
			
			addItem("Settings...", new Command() {
				public void execute() {
					// TODO Auto-generated method stub
					// Settings window (at this time) doesn't work as an "edit" only a "new"
					myFtp.show();
					myFtp.setFTPSite(ftpSite);
				}
			});
			
			addItem("Duplicate...", new Command() {
				public void execute() {
					// TODO Auto-generated method stub
					// Should add a new connection and automatically bring up settings window
					FTPSite duplicateFTPSite = new FTPSite();
					
					duplicateFTPSite.setUserId(ftpSite.getUserId());
					duplicateFTPSite.setHost(ftpSite.getHost());
					duplicateFTPSite.setPort(ftpSite.getPort());
					duplicateFTPSite.setUsername(ftpSite.getUsername());
					duplicateFTPSite.setPassword(ftpSite.getPassword());
					
					myFtp.show();
					myFtp.setFTPSite(duplicateFTPSite);
				}
			});
			
			addItem("Delete", new Command() {
				public void execute() {
					// TODO Auto-generated method stub
					// obviously should have a confirmation
					if(Window.confirm("Delete FTP Connection?\n\n" + ftpSite.toString())){
						FTPService.Util.getInstance().deleteUserFTPSite(ftpSite, new AsyncCallback() {
							public void onFailure(Throwable caught) {
								Window.alert("Failed to delete FTP connection:\n" + ftpSite.toString() + "\n\nMessage:\n" + caught.getMessage());
							}
							public void onSuccess(Object result) {
								refreshListFromServer();
							}
						});
					}
				}
			});
		}
	}
}
