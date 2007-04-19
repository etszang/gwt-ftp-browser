/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jkelling
 *
 */
public class FTPConnectionSettingsFormPanel extends Composite implements ClickListener {
	
	private Label instructions;
	private final HeaderLabel headerLabelForSettingsPanel;
	private final VerticalPanel layoutPanel = new VerticalPanel();
	private final FTPConnectionSettingsFieldsGrid fieldsGrid = new FTPConnectionSettingsFieldsGrid();
	private final Button saveButton = new Button("Save", this);
	private final Button cancelButton = new Button("Cancel", this);
	private final Vector ftpConnectionSettingsListenerCollection = new Vector();
	
	public FTPConnectionSettingsFormPanel() {
		initWidget(layoutPanel);
		
		headerLabelForSettingsPanel = new HeaderLabel("FTP Connection Settings", false);
		headerLabelForSettingsPanel.addStyleName("settings-panel-header");
		
		layoutPanel.add(headerLabelForSettingsPanel);
		instructions = new Label("");
		layoutPanel.add(instructions);
		layoutPanel.add(fieldsGrid);
		
		HorizontalPanel buttonsPanel = new HorizontalPanel();
		
		buttonsPanel.add(saveButton);
		buttonsPanel.add(cancelButton);
		
		layoutPanel.add(buttonsPanel);
	}
	
	public void setFTPSite(FTPSite conn) {
		fieldsGrid.setFTPSite(conn);
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
			listener.onFTPConnectionSettingsSave(fieldsGrid.getFTPSiteUpdated());
			
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
			//layoutPanel.setVisible(false);
			fireFTPConnectionSettingsCancel();
		}
	}
	public String getTitle() {
		return headerLabelForSettingsPanel.getText();
	}
	public void setTitle(String text) {
		headerLabelForSettingsPanel.setText(text);
	}
	public String getInstructionsText() {
		return instructions.getText();
	}
	public void setInstructionsText(String text) {
		instructions.setText(text);
	}
}
