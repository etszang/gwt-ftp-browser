/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.Iterator;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Jkelling
 *
 */
public class FTPBrowser extends Composite implements LoadsRemoteData {
	
	private final VerticalPanel vertPanel = new VerticalPanel();
	private final HorizontalPanel directoryBrowserPanel = new HorizontalPanel();
	/*private final Button parentLevelButton = new Button("Go up one directory");
	private final Button openDirectoryButton = new Button("Open selection");*/

	private final FTPFileItemSelectGrid selectGrid = new FTPFileItemSelectGrid();
	private FTPConnectionsMenuBar ftpConnectionsMenuBar = null;
	private FTPTree ftpTree = null;

	public FTPBrowser() {
		initWidget(vertPanel);

		addStyleName("gwt-ftpbrowser");
		
		ftpTree = new FTPTree();
		ftpConnectionsMenuBar = new FTPConnectionsMenuBar(ftpTree);

		ScrollPanel sc1 = new ScrollPanel(ftpTree);
		ScrollPanel sc2 = new ScrollPanel(selectGrid);

		sc1.setSize("100%", "100%");
		sc2.setSize("100%", "100%");
		
		sc1.setAlwaysShowScrollBars(true);
		sc2.setAlwaysShowScrollBars(true);
		
	    DOM.setStyleAttribute(sc1.getElement(), "overflowX", "auto");
	    DOM.setStyleAttribute(sc2.getElement(), "overflowX", "auto");
		
		final MenuBar ftpBrowserMenuBar = new MenuBar(false);
		final MenuItem ftpSitesMenuItem = new MenuItem("FTP Sites", false, (MenuBar) ftpConnectionsMenuBar);
		ftpBrowserMenuBar.addStyleName("ftpbrowser-menubar");
		ftpSitesMenuItem.addStyleName("ftpbrowser-menuitem");
		ftpBrowserMenuBar.addItem(ftpSitesMenuItem);
		
		directoryBrowserPanel.add(sc1);
		directoryBrowserPanel.add(sc2);
		
		vertPanel.add(ftpBrowserMenuBar);
		vertPanel.add(directoryBrowserPanel);
		
		vertPanel.setWidth("100%");
		directoryBrowserPanel.setWidth("100%");
		
		directoryBrowserPanel.setCellWidth(sc1, "50%");
		directoryBrowserPanel.setCellWidth(sc2, "50%");
		
		directoryBrowserPanel.setCellHeight(sc1, "196px");
		directoryBrowserPanel.setCellHeight(sc2, "196px");
		
		//directoryBrowserPanel.add(parentLevelButton);
		//directoryBrowserPanel.add(directoryListPanel);
		//directoryBrowserPanel.add(openDirectoryButton);

		/*parentLevelButton.addClickListener(this);
		openDirectoryButton.addClickListener(this);*/
		
		ftpTree.addTreeListener(new TreeListener());
	}
	
	public FTPConnectionsMenuBar getFTPConnectionsMenuBar() {
		return ftpConnectionsMenuBar;
	}

	public FTPTree getFTPTree() {
		return ftpTree;
	}

	public FTPFileItemSelectGrid getFTPFileItemSelectGrid() {
		return selectGrid;
	}

	private class TreeListener implements FTPTreeListener {

		public void onTreeItemSelected(final TreeItem item) {
			if (item instanceof FTPTreeItem)
				onTreeItemSelected((FTPTreeItem) item);
		}
		
		private void onTreeItemSelected(final FTPTreeItem item) {
			if (selectGrid != null)
				selectGrid.clear();

			if (item.hasData() || ! item.getNeedsToLoad()) {
				final TreeItem selectedTreeItem = ftpTree.getSelectedItem();
				DeferredCommand.add(new Command() {
					public void execute() {
						if (selectGrid != null)
							selectGrid.clear();
						else
							return;

						FTPTreeItem ftpTreeItem = null;

						for (Iterator it = item.getFileItems().iterator(); it.hasNext();) {
							if (! selectedTreeItem.equals(ftpTree.getSelectedItem()))
								return; // Prevents unnecessary processing
							ftpTreeItem = (FTPTreeItem) it.next();
							if (selectGrid != null && ftpTreeItem.getFTPFileItem().getType().equals("f"))
								selectGrid.addItem(ftpTreeItem.getFTPFileItem().clone());
						}
					}
				});
			}
		}

		public void onTreeItemStateChanged(TreeItem item) {
		}

		public void afterDataReceived(final FTPTreeItem parentTreeItem) {
			FTPFileItem ftpFileItem = null;
			
			if (selectGrid != null && parentTreeItem.equals(ftpTree.getSelectedItem()))
				selectGrid.clear();
			
			for (Iterator it = parentTreeItem.getFileItems().iterator(); it.hasNext();) {
				ftpFileItem = ((FTPTreeItem) it.next()).getFTPFileItem();
				
				if (selectGrid != null && parentTreeItem.equals(ftpTree.getSelectedItem()))
					selectGrid.addItem((FTPFileItem) ftpFileItem);
			}
		}

	}
	
	public void reloadRemoteData() {
		reloadRemoteData(null);
	}
	public void reloadRemoteData(AsyncCallback callback) {
		ftpConnectionsMenuBar.reloadRemoteData(callback);
	}
}
