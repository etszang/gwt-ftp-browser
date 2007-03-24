/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

/**
 * @author Jkelling
 * 
 */
public class FTPConnectionSettingsPopupPanel extends PopupOverlayPanel {

	private final FTPConnectionSettingsFormPanel formPanel = new FTPConnectionSettingsFormPanel();

	public FTPConnectionSettingsPopupPanel() {
		super();

		setWidget(formPanel);
	}

	public FTPConnectionSettingsPopupPanel(boolean autoHide) {
		super(autoHide);

		setWidget(formPanel);
	}
	
	public void addFTPConnectionSettingsListener(FTPConnectionSettingsListener listener) {
		formPanel.addFTPConnectionSettingsListener(listener);
	}
	
	public void removeFTPConnectionSettingsListener(FTPConnectionSettingsListener listener) {
		formPanel.removeFTPConnectionSettingsListener(listener);
	}
	
}
