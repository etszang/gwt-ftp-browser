/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ResponseTextHandler;
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
	private FTPSite myConnect = null;
	
	public FTPConnectionsMenuBar(FTPTree ftpTree) {
		super(true);
		
		this.ftpTree = ftpTree;
		
		setAutoOpen(true);
		addStyleName("ftpconnections-menubar");
		
		myFtp.addFTPConnectionSettingsListener(this);
		
		refreshListFromServer();
	}
	
	public void onFTPConnectionSettingsSave(FTPSite result) {
		myConnect.setUsername(result.getUsername());
		myConnect.setPort(result.getPort());
		myConnect.setPassword(result.getPassword());
		myConnect.setHost(result.getHost());
		myFtp.hide();
	}
	
	public void onFTPConnectionSettingsCancel() {
		myFtp.hide();
	}
	
	protected FTPTree getFTPTree() {
		return ftpTree;
	}
	
	private void refreshListFromServer() {
		//HTTPRequest.asyncGet(GWT.getModuleBaseURL()+CookieCloaker.DEFAULT_INSTANCE.ftpConnectionListURL(), new ListBuilder());
		
		FTPService.Util.getInstance().getUserFTPSites(7, new ListBuilder());
	}
	
	private class ListBuilder implements ResponseTextHandler, AsyncCallback {
		public void onCompletion(String responseText) {
			final JSONResponse jsonResponse = JSONResponse.newInstance(responseText);
			final JSONArray connections = (JSONArray) jsonResponse.getResult().get("connectionList");
			FTPSite conn = null;
			
			for (int i = 0; i < connections.size(); i++) {
				conn = FTPSiteFactory.getInstance((JSONObject) connections.get(i));
				addItem(new ConnectionMenuItem(conn));
			}
		}

		public void onFailure(Throwable caught) {
			Window.alert(caught.getMessage());
		}

		public void onSuccess(Object result) {
			List ftpSites = (List) result;
			FTPSite conn = null;
			for (Iterator it = ftpSites.iterator(); it.hasNext();)
				addItem(new ConnectionMenuItem((FTPSite) it.next()));
		}
	}
	
	private class ConnectionMenuItem extends MenuItem {
		public ConnectionMenuItem(FTPSite ftpConnection) {
			super(ftpConnection.getHost(), true, new SubMenu(ftpConnection));
			addStyleName("ftpconnections-menuitem");
		}
	}
	
	private class SubMenu extends MenuBar {
		public SubMenu(final FTPSite ftpConnection) {
			super(true);
			addStyleName("ftpconnections-submenubar");
			
			addItem("Connect", new Command() {
				public void execute() {
					if (getFTPTree() != null)
						getFTPTree().setFTPConnection(ftpConnection);
				}
			});
			
			addItem("Settings...", new Command() {
				public void execute() {
					// TODO Auto-generated method stub
					// Settings window (at this time) doesn't work as an "edit" only a "new"
					myFtp.show();
					myFtp.getSettings(ftpConnection);
					myConnect = ftpConnection;
					//Window.alert("TODO Settings window (at this time) doesn't work as an \"edit\" only a \"new\"");
				}
			});
			
			addItem("Duplicate...", new Command() {
				public void execute() {
					// TODO Auto-generated method stub
					// Should add a new connection and automatically bring up settings window
					addItem(new ConnectionMenuItem(ftpConnection));
					myFtp.show();
					myFtp.getSettings(ftpConnection);
					myConnect = ftpConnection;
					Window.alert("TODO: Should add a new connection (PHP) and automatically bring up settings window");
				}
			});
			
			addItem("Delete", new Command() {
				public void execute() {
					// TODO Auto-generated method stub
					// obviously should have a confirmation
					if(Window.confirm("Delete Ftp Connection?")){
						
					}
				}
			});
		}
	}
}
