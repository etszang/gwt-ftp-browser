package com.jonosoft.ftpbrowser.web.client;

import org.gwtwidgets.client.ui.EditableLabel;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


public class FTPConnectionSettingsFieldsGrid extends Composite {
	private FTPSite ftpSite = null;
	private final Grid grid = new Grid(4, 2);
	private final TextBox host = new TextBox();
	private final PasswordTextBox password = new PasswordTextBox();
	private final EditableLabel port = new EditableLabel("21", new ChangeListener(){public void onChange(Widget sender) {}}, "Change", "Cancel");
	private final TextBox username = new TextBox();
	
	public FTPConnectionSettingsFieldsGrid() {
		initWidget(grid);
		
		grid.setText(0, 0, "Host:");
		grid.setText(1, 0, "Port:");
		grid.setText(2, 0, "Username:");
		grid.setText(3, 0, "Password:");
		
		grid.setWidget(0, 1, host);
		grid.setWidget(1, 1, port);
		grid.setWidget(2, 1, username);
		grid.setWidget(3, 1, password);
	}
	
	public FTPSite getFTPSiteUpdated() {
		ftpSite.setHost(host.getText());
		ftpSite.setPort(Integer.parseInt(port.getText()));
		ftpSite.setUsername(username.getText());
		ftpSite.setPassword(password.getText());
		return ftpSite;
	}
	
	public void getFTPSite(FTPSite site) {
		site.setFtpSiteId(this.ftpSite.getFtpSiteId());
		site.setUserId(this.ftpSite.getUserId());
		site.setHost(host.getText());
		site.setPort(Integer.parseInt(port.getText()));
		site.setUsername(username.getText());
		site.setPassword(password.getText());
	}
	
	public void setFTPSite(FTPSite site){
		this.ftpSite = site;
		host.setText(emptyIfNull(site.getHost()));
		port.setText(emptyIfNull(Integer.toString(site.getPort())));
		username.setText(emptyIfNull(site.getUsername()));
		password.setText(emptyIfNull(site.getPassword()));
	}
	
	private static String emptyIfNull(String text) {
		return (text == null) ? "" : text;
	}
}