/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.Iterator;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jkelling
 *
 */
public class FTPBrowser extends Composite {
	
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

		sc1.setPixelSize(375, 196);
		sc2.setPixelSize(325, 196);
		
		final MenuBar ftpBrowserMenuBar = new MenuBar(false);
		final MenuItem ftpSitesMenuItem = new MenuItem("<a href=\"javascript:void(0);\"><span>FTP Sites</span></a>", true, (MenuBar) ftpConnectionsMenuBar);
		ftpBrowserMenuBar.addStyleName("ftpbrowser-menubar");
		ftpSitesMenuItem.addStyleName("ftpbrowser-menuitem");
		ftpBrowserMenuBar.addItem(ftpSitesMenuItem);
		
		vertPanel.add(ftpBrowserMenuBar);
		vertPanel.add(directoryBrowserPanel);

		directoryBrowserPanel.add(sc1);
		directoryBrowserPanel.add(sc2);

		//directoryBrowserPanel.add(parentLevelButton);
		//directoryBrowserPanel.add(directoryListPanel);
		//directoryBrowserPanel.add(openDirectoryButton);

		/*parentLevelButton.addClickListener(this);
		openDirectoryButton.addClickListener(this);*/
		
		ftpTree.addTreeListener(new TreeListener());
	}

	public void changeDirectory(String path) {

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
				final com.google.gwt.user.client.ui.TreeItem selectedTreeItem = ftpTree.getSelectedItem();
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
	
}
