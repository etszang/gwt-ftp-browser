package com.jonosoft.ftpbrowser.web.client;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

public class FTPSiteMenuItem extends MenuItem {
	
	public interface FTPMenuItemCommandListener {
		public void onFTPMenuItemConnect(FTPSite ftpSite);
		public void onFTPMenuItemsChanged();
	}
	
	private class FTPMenuItemCommandListenerCollection extends Vector {
		private static final long serialVersionUID = -4182532120888959460L;
		private void fireFTPMenuItemConnect(FTPSite ftpSite) {
			if (listenerCollection != null)
				for (Iterator it = iterator(); it.hasNext();)
					((FTPMenuItemCommandListener) it.next()).onFTPMenuItemConnect(ftpSite);
		}
		private void fireFTPMenuItemsChanged() {
			if (listenerCollection != null)
				for (Iterator it = iterator(); it.hasNext();)
					((FTPMenuItemCommandListener) it.next()).onFTPMenuItemsChanged();
		}
	}

	private FTPMenuItemCommandListenerCollection listenerCollection = null;

	public FTPSiteMenuItem(FTPSite ftpSite, FTPMenuItemCommandListener listener) {
		super((ftpSite == null) ? "New FTP Site..." : ftpSite.getHost(), true, (MenuBar)null);
		
		if (ftpSite != null) {
			setSubMenu(new FTPConnectionSubMenu(ftpSite));
			addStyleName("ftpconnections-menuitem");
			addFTPMenuItemCommandListener(listener);
		}
		else {
			setCommand(new Command() {
				public void execute() {
					FTPSite newFTPSite = new FTPSite();
					
					// The default port for FTP is 21
					newFTPSite.setPort(new Integer(21));
					
					createAndShowNewSettingsDialog(newFTPSite, getFTPMenuItemCommandListenerCollection());
				}
			});
		}
	}
	
	private FTPMenuItemCommandListenerCollection getFTPMenuItemCommandListenerCollection() {
		if (listenerCollection == null)
			listenerCollection = new FTPMenuItemCommandListenerCollection();
		return listenerCollection;
	}
	
	public static void createAndShowNewSettingsDialog(final FTPSite ftpSite, final FTPMenuItemCommandListenerCollection listenerCollection) {
		final FTPConnectionSettingsPopupPanel settingsDialog = new FTPConnectionSettingsPopupPanel(true);
		
		settingsDialog.addFTPConnectionSettingsListener(new FTPConnectionSettingsListener() {
			public void onFTPConnectionSettingsCancel() {
				settingsDialog.hide();
			}
			public void onFTPConnectionSettingsSave(final FTPSite result) {
				FTPService.Util.getInstance().saveUserFTPSite(result, new AsyncCallback() {
					public void onFailure(Throwable caught) {
						Window.alert("Unexpected failure saving the FTP Connection. If this continues, please contact our staff.\n\nMessage:\n" + caught.getMessage());
						listenerCollection.fireFTPMenuItemsChanged();
					}
					
					public void onSuccess(Object result) {
						listenerCollection.fireFTPMenuItemConnect((FTPSite) result);
						listenerCollection.fireFTPMenuItemsChanged();
						Window.alert("FTP site has been saved.\n\n" + result.toString());
					}
				});
				settingsDialog.hide();
			}
		});
		
		settingsDialog.show();
	}

	public void addFTPMenuItemCommandListener(FTPMenuItemCommandListener listener) {
		getFTPMenuItemCommandListenerCollection().add(listener);
	}

	public void removeFTPMenuItemCommandListener(FTPMenuItemCommandListener listener) {
		if (listenerCollection != null)
			listenerCollection.remove(listener);
	}

	private class FTPConnectionSubMenu extends MenuBar {
		public FTPConnectionSubMenu(final FTPSite ftpSite) {
			super(true);
			addStyleName("ftpconnections-submenubar");

			addItem("Connect", new Command() {
				public void execute() {
					listenerCollection.fireFTPMenuItemConnect(ftpSite);
				}
			});

			addItem("Settings...", new Command() {
				public void execute() {
					createAndShowNewSettingsDialog(ftpSite, listenerCollection);
				}
			});

			addItem("Duplicate...", new Command() {
				public void execute() {
					// Should add a new connection and automatically bring up settings window
					FTPSite duplicateFTPSite = new FTPSite();

					duplicateFTPSite.setUserId(ftpSite.getUserId());
					duplicateFTPSite.setHost(ftpSite.getHost());
					duplicateFTPSite.setPort(ftpSite.getPort());
					duplicateFTPSite.setUsername(ftpSite.getUsername());
					duplicateFTPSite.setPassword(ftpSite.getPassword());

					createAndShowNewSettingsDialog(duplicateFTPSite, listenerCollection);
				}
			});

			addItem("Delete", new Command() {
				public void execute() {
					// obviously should have a confirmation
					if(Window.confirm("Delete FTP Connection?\n\n" + ftpSite.toString())){
						FTPService.Util.getInstance().deleteUserFTPSite(ftpSite, new AsyncCallback() {
							public void onFailure(Throwable caught) {
								Window.alert("Failed to delete FTP connection:\n" + ftpSite.toString() + "\n\nMessage:\n" + caught.getMessage());
							}
							public void onSuccess(Object result) {
								listenerCollection.fireFTPMenuItemsChanged();
							}
						});
					}
				}
			});
		}
	}
}