/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.Iterator;
import java.util.Vector;

import org.gwtwidgets.client.ui.EditableLabel;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jkelling
 *
 */
public class FTPConnectionSettingsFormPanel extends Composite implements ClickListener {
	
	private final VerticalPanel layoutPanel = new VerticalPanel();
	private final FieldsGrid fieldsGrid = new FieldsGrid();
	private final Button saveButton = new Button("Save", this);
	private final Button cancelButton = new Button("Cancel", this);
	private final Vector ftpConnectionSettingsListenerCollection = new Vector();
	
	public FTPConnectionSettingsFormPanel() {
		initWidget(layoutPanel);
		
		Label headerLabelForSettingsPanel = new Label("FTP Connection Settings", false);
		headerLabelForSettingsPanel.addStyleName("settings-panel-header");
		
		layoutPanel.add(headerLabelForSettingsPanel);
		layoutPanel.add(fieldsGrid);
		
		HorizontalPanel buttonsPanel = new HorizontalPanel();
		
		buttonsPanel.add(saveButton);
		buttonsPanel.add(cancelButton);
		
		layoutPanel.add(buttonsPanel);
	}
	
	public void addFTPConnectionSettingsListener(FTPConnectionSettingsListener listener) {
		ftpConnectionSettingsListenerCollection.add(listener);
	}
	
	public void removeFTPConnectionSettingsListener(FTPConnectionSettingsListener listener) {
		ftpConnectionSettingsListenerCollection.remove(listener);
	}
	
	private void fireFTPConnectionSettingsSave() {
		for (Iterator it = ftpConnectionSettingsListenerCollection.iterator(); it.hasNext();) {
			FTPConnectionSettingsListener listener = (FTPConnectionSettingsListener) it.next();
			listener.onFTPConnectionSettingsSave(fieldsGrid.getFTPConnection());
		}
	}
	
	private void fireFTPConnectionSettingsCancel() {
		for (Iterator it = ftpConnectionSettingsListenerCollection.iterator(); it.hasNext();) {
			FTPConnectionSettingsListener listener = (FTPConnectionSettingsListener) it.next();
			listener.onFTPConnectionSettingsCancel();
		}
	}
	
	public void onClick(Widget sender) {
		if (sender.equals(saveButton))
		{
			fireFTPConnectionSettingsSave();
		}
		else if (sender.equals(cancelButton))
		{
			fireFTPConnectionSettingsCancel();
		}
	}

	private class FieldsGrid extends Composite {
		private final Grid grid = new Grid(4, 2);
		private final TextBox host = new TextBox();
		private final EditableLabel port = new EditableLabel("21", new ChangeListener(){public void onChange(Widget sender) {}}, "Change", "Cancel");
		private final TextBox username = new TextBox();
		private final PasswordTextBox password = new PasswordTextBox();
		
		public FieldsGrid() {
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
		
		public FTPConnection getFTPConnection() {
			FTPConnection conn = new FTPConnection();
			getFTPConnection(conn);
			return conn;
		}
		
		public void getFTPConnection(FTPConnection conn) {
			conn.setServer(host.getText());
			conn.setPort(Integer.parseInt(port.getText()));
			conn.setUsername(username.getText());
			conn.setPassword(password.getText());
		}
	}
	
}
