/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jkelling
 *
 */
public class FTPBrowser extends Composite {
	private final HorizontalPanel directoryBrowserPanel = new HorizontalPanel();
	/*private final Button parentLevelButton = new Button("Go up one directory");
	private final Button openDirectoryButton = new Button("Open selection");*/
	private final SelectionChangedListenerCollection listenerCollection = new SelectionChangedListenerCollection();

	private final FTPFileItemSelectGrid ftpFileSelectGrid = new FTPFileItemSelectGrid();
	private final FTPConnectionsMenuBar ftpConnectionsMenuBar = new FTPConnectionsMenuBar();
	private FTPTree ftpTree = null;

	public FTPBrowser() {
		initWidget(directoryBrowserPanel);

		addStyleName("gwt-ftpbrowser");

		ScrollPanel sc2 = new ScrollPanel(ftpFileSelectGrid);
		ScrollPanel sc1 = new ScrollPanel(ftpTree = new FTPTree());

		sc1.setPixelSize(350, 300);
		sc2.setPixelSize(200, 300);

		directoryBrowserPanel.add(ftpConnectionsMenuBar);
		directoryBrowserPanel.add(sc1);
		directoryBrowserPanel.add(sc2);

		//directoryBrowserPanel.add(parentLevelButton);
		//directoryBrowserPanel.add(directoryListPanel);
		//directoryBrowserPanel.add(openDirectoryButton);

		/*parentLevelButton.addClickListener(this);
		openDirectoryButton.addClickListener(this);*/

		ftpTree.addTreeListener(new FTPTreeListener());
	}

	public void changeDirectory(String path) {

	}

	public void addSelectionChangedListener(SelectionChangedListener listener) {
		listenerCollection.add(listener);
	}

	public void onClick(Widget sender) {

	}

	public FTPConnectionsMenuBar getFTPConnectionsMenuBar() {
		return ftpConnectionsMenuBar;
	}

	public FTPTree getFTPTree() {
		return ftpTree;
	}

	public FTPFileItemSelectGrid getFTPFileItemSelectGrid() {
		return ftpFileSelectGrid;
	}

	private class FTPTreeListener implements TreeListener {

		public void onTreeItemSelected(TreeItem item) {
			
		}

		public void onTreeItemStateChanged(TreeItem item) {
			// TODO Auto-generated method stub

		}

	}

}
