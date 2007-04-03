/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.HTTPRequest;
import com.google.gwt.user.client.ResponseTextHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * @author Jkelling
 *
 */
public class FTPConnectionsMenuBar extends Composite implements FTPConnectionSettingsListener   {
	
	private final MenuBar connectionsMenu = new MenuBar(true);
	private final FTPConnectionSettingsPopupPanel myFtp = new FTPConnectionSettingsPopupPanel(true);
	private  FTPConnection myConnect;
	
	public FTPConnectionsMenuBar() {
		initWidget(connectionsMenu);
		
		connectionsMenu.setAutoOpen(true);
		connectionsMenu.addStyleName("ftpconnections-menubar");
		
		myFtp.addFTPConnectionSettingsListener(this);
		
		refreshListFromServer();
	}
	
	public void onFTPConnectionSettingsSave(FTPConnection result) {
		myConnect.setUsername(result.getUsername());
		myConnect.setPort(result.getPort());
		myConnect.setPassword(result.getPassword());
		myConnect.setServer(result.getServer());
		myFtp.hide();
	}
	
	public void onFTPConnectionSettingsCancel() {
		myFtp.hide();
	}
	
	private void refreshListFromServer() {
		HTTPRequest.asyncGet(GWT.getModuleBaseURL()+CookieCloaker.DEFAULT_INSTANCE.ftpConnectionListURL(), new ListBuilder());
	}
	
	private class ListBuilder implements ResponseTextHandler {
		public void onCompletion(String responseText) {
			final JSONResponse jsonResponse = JSONResponse.newInstance(responseText);
			final JSONArray connections = (JSONArray) jsonResponse.getResult().get("connectionList");
			FTPConnection conn = null;
			
			for (int i = 0; i < connections.size(); i++) {
				conn = FTPConnection.getInstance((JSONObject) connections.get(i));
				connectionsMenu.addItem(new ConnectionMenuItem(conn));
			}
		}
	}
	
	private class ConnectionMenuItem extends MenuItem {
		public ConnectionMenuItem(FTPConnection ftpConnection) {
			super(ftpConnection.getServer(), new SubMenu(ftpConnection));
			addStyleName("ftpconnections-menuitem");
		}
	}
	
	private class SubMenu extends MenuBar {
		public SubMenu(final FTPConnection ftpConnection) {
			super(true);
			addStyleName("ftpconnections-submenubar");
			
			addItem("Connect", new Command() {
				public void execute() {
					Web.getFTPBrowser().getFTPTree().setFTPConnection(ftpConnection);
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
					connectionsMenu.addItem(new ConnectionMenuItem(ftpConnection));
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
