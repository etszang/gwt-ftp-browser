/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

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
public class FTPConnectionsMenuBar extends Composite {
	
	private final MenuBar connectionsMenu = new MenuBar(true);
	
	public FTPConnectionsMenuBar() {
		initWidget(connectionsMenu);
		
		connectionsMenu.setAutoOpen(true);
		connectionsMenu.addStyleName("ftpconnections-menubar");
		
		refreshListFromServer();
	}
	
	private void refreshListFromServer() {
		HTTPRequest.asyncGet(CookieCloaker.DEFAULT_INSTANCE.ftpConnectionListURL(), new ListBuilder());
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
					// TODO Auto-generated method stub
					// Clear tree and set to use new connection
					Window.alert("TODO: Clear tree and set to use new connection. " + ftpConnection.getServer());
				}
			});
			
			addItem("Settings...", new Command() {
				public void execute() {
					// TODO Auto-generated method stub
					// Settings window (at this time) doesn't work as an "edit" only a "new"
					Window.alert("TODO: Settings window (at this time) doesn't work as an \"edit\" only a \"new\"");
				}
			});
			
			addItem("Duplicate...", new Command() {
				public void execute() {
					// TODO Auto-generated method stub
					// Should add a new connection and automatically bring up settings window
					Window.alert("TODO: Should add a new connection (PHP) and automatically bring up settings window");
				}
			});
			
			addItem("Delete", new Command() {
				public void execute() {
					// TODO Auto-generated method stub
					// obviously should have a confirmation
					Window.alert("TODO: Use PHP to delete. obviously should have a confirmation");
				}
			});
		}
	}
	
}
