/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jkelling
 *
 */
public class FTPBrowser extends Composite {
	private final HorizontalPanel panel = new HorizontalPanel();
	
	public FTPBrowser() {
		initWidget(panel);
		new FTPDirectoryBrowser();
	}
	
	private class FTPDirectoryBrowser extends Composite implements ClickListener {
		private final HorizontalPanel directoryBrowserPanel = new HorizontalPanel();
		private final HorizontalPanel directoryListPanel = new HorizontalPanel();
		private final Button parentLevelButton = new Button("Go up one directory");
		private final Button openDirectoryButton = new Button("Open selection");
		private final SelectionChangedListenerCollection listenerCollection = new SelectionChangedListenerCollection();
		
		public FTPDirectoryBrowser() {
			initWidget(directoryBrowserPanel);
			
			directoryBrowserPanel.add(parentLevelButton);
			directoryBrowserPanel.add(directoryListPanel);
			directoryBrowserPanel.add(openDirectoryButton);
			
			parentLevelButton.addClickListener(this);
			openDirectoryButton.addClickListener(this);
		}
		
		public void changeDirectory(String path) {
			
		}
		
		public void addSelectionChangedListener(SelectionChangedListener listener) {
			listenerCollection.add(listener);
		}

		public void onClick(Widget sender) {
			
		}
	}
	
}
