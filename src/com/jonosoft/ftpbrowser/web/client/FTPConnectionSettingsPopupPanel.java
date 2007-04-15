/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.user.client.Window;

/**
 * @author Jkelling
 * 
 */
public class FTPConnectionSettingsPopupPanel extends PopupOverlayPanel {

	private final FTPConnectionSettingsFormPanel formPanel = new FTPConnectionSettingsFormPanel();

	public FTPConnectionSettingsPopupPanel() {
		super();
		setWidget(formPanel);
		init();
	}

	public FTPConnectionSettingsPopupPanel(boolean autoHide) {
		super(autoHide);
		setWidget(formPanel);
		init();
	}
	
	public void init() {
		addStyleName("ftp-popuppanel");
	}
	
	public void show() {
		super.show();
		setPopupPosition(
				(Window.getClientWidth() / 2) - (getOffsetWidth() / 2),
				(Window.getClientHeight() / 2) - (getOffsetHeight() / 2)
		);
	}
	
	public void addFTPConnectionSettingsListener(FTPConnectionSettingsListener listener) {
		formPanel.addFTPConnectionSettingsListener(listener);
	}
	
	public void setFTPSite(FTPSite site){
		formPanel.setFTPSite(site);
	}
	
	public void removeFTPConnectionSettingsListener(FTPConnectionSettingsListener listener) {
		formPanel.removeFTPConnectionSettingsListener(listener);
	}
	
}
