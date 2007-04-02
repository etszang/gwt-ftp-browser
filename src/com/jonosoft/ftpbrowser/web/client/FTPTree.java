/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SourcesTreeEvents;
import com.google.gwt.user.client.ui.TreeListener;

/**
 * @author Jkelling
 * 
 */
public class FTPTree extends Composite implements TreeListener, SourcesTreeEvents {
	private final Tree ftpTree = new Tree();
	//private final VerticalPanel myPan = new VerticalPanel();
	private final Tree tempTree = new Tree();
	private FTPConnection ftpConnection = null;
	// private String fileExtension[];
	private ArrayList myList;

	public FTPTree() {
		initWidget(ftpTree);
		
		addStyleName("gwt-ftptree");
		
		myList = new ArrayList();
		myList.add("php");
		myList.add("css");
		
		ftpTree.addTreeListener(this);
		tempTree.addTreeListener(this);
	}
	
	public void setFTPConnection(FTPConnection ftpConnection) {
		this.ftpConnection = ftpConnection;
		ftpTree.clear();
		//ftpConnection.getList("/", new FTPGetDirectoryContentsResponseHandler((HasTreeItems) ftpTree));
		final FTPTreeItem ftpTreeItem = new FTPTreeItem(new FTPFileItem(ftpConnection, "/" + ftpConnection.getServer(), "d", "/"));
		ftpTreeItem.setData();
		ftpTreeItem.setNeedsToLoad(false);
		ftpTree.addItem(ftpTreeItem);
		ftpConnection.getList("/", new FTPGetDirectoryContentsResponseHandler((HasTreeItems) ftpTreeItem));
		DeferredCommand.add(new Command() {
			// When setFTPConnection is called from the constructor isAttached() won't be true right away
			public void execute() {
				if (isAttached())
					ftpTreeItem.setState(true);
			}
		});
	}

	public void setRootPath() {
		ftpTree.addTreeListener(this);
	}

	/*public void extenList(ArrayList iList) {
		myList = iList;
	}*/

	public void onTreeItemSelected(com.google.gwt.user.client.ui.TreeItem item) {
		if (item instanceof FTPTreeItem) {
			onTreeItemSelected((FTPTreeItem) item);
		}
	
		/*final FTPTreeItem ftpItem = (FTPTreeItem) item;
		if (ftpItem.hasData() == false) {
			ftpConnection.getList(ftpItem.getFTPFileItem().getFullPath(), new FTPGetDirectoryContentsResponseHandler((HasTreeItems) ftpItem));
			ftpItem.setData();
		}
		if (myPan != null) {
			myPan.clear();
		}
		for (int i = 0; i < ftpItem.getChildCount(); i++) {
			CheckBox temp = new CheckBox(ftpItem.getChild(i).getText());
			temp.setVisible(true);
			myPan.add(temp);*/
			/*
			String[] test = (ftpItem.getChild(i).getText().split("\\."));
			if (test.length > 1) {
				System.out.println(myList);
				if (myList.contains(test[1])) {
					CheckBox temp = new CheckBox(ftpItem.getChild(i).getText());
					temp.setVisible(true);
					myPan.add(temp);
				} else {
					CheckBox temp = new CheckBox(ftpItem.getChild(i).getText());
					temp.setVisible(false);
					myPan.add(temp);
				}
			} else {
				CheckBox temp = new CheckBox(ftpItem.getChild(i).getText());
				temp.setVisible(true);
				myPan.add(temp);
			}
		}*/
	}
	
	private void onTreeItemSelected(final FTPTreeItem item) {
		final ItemSelectGrid selectGrid = Web.getFTPBrowser().getFTPFileItemSelectGrid();;
		
		if (selectGrid != null)
			selectGrid.clear();
		
		if (! item.hasData() && item.getNeedsToLoad()) {
			// if item.getNeedsToLoad() is true, then there should be a temporary child
			// TreeItem that needs to be removed, but it's safe to remove them all anyway
			//item.removeItems();
			DeferredCommand.add(new Command() {
				public void execute() {
					// Using a DeferredCommand to execute this makes for a
					// snappier UI; oherwise, there would be a short delay
					// after clicking before the items appears selected.
					ftpConnection.getList(item.getFTPFileItem().getFullPath(), new FTPGetDirectoryContentsResponseHandler((HasTreeItems) item));
				}
			});
			item.setData();
			item.setNeedsToLoad(false);
		}
		else {
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

	/*public VerticalPanel myPanel() {
		return myPan;
	}*/

	public void onTreeItemStateChanged(com.google.gwt.user.client.ui.TreeItem item) {
		if (item instanceof FTPTreeItem) {
			onFTPTreeItemStateChanged((FTPTreeItem) item);
		}
	}

	private void onFTPTreeItemStateChanged(final FTPTreeItem item) {
		if (item.getState()) {
			if (! item.hasData() && item.getNeedsToLoad()) {
				// if item.getNeedsToLoad() is true, then there should be a temporary child
				// TreeItem that needs to be removed, but it's safe to remove them all anyway
				//item.removeItems();
				DeferredCommand.add(new Command() {
					public void execute() {
						// Using a DeferredCommand to execute this makes for a
						// snappier UI; oherwise, there would be a short delay
						// after clicking before the items appears selected.
						ftpConnection.getList(item.getFTPFileItem().getFullPath(), new FTPGetDirectoryContentsResponseHandler((HasTreeItems) item));
					}
				});
				item.setData();
				item.setNeedsToLoad(false);
			}
			ftpTree.setSelectedItem(item);
		}
	}

	protected class FTPGetDirectoryContentsResponseHandler implements
			FTPAsyncCallback {
		private HasTreeItems parentTreeItem = null;

		public FTPGetDirectoryContentsResponseHandler(HasTreeItems parentTreeItem) {
			this.parentTreeItem = parentTreeItem;
		}

		public void onFailure(Throwable caught) {
			try {
				throw caught;
			} catch (Throwable ignoredException) {
			}
		}

		public void onSuccess(Object result) {
			FTPTreeItem ftpTreeItem = null;
			
			if (result instanceof List) {
				final List ar = (List) result;
				final Iterator it = ar.iterator();
				ItemSelectGrid selectGrid = null;
				
				if (Web.getFTPBrowser() != null)
					selectGrid = Web.getFTPBrowser().getFTPFileItemSelectGrid();
				
				parentTreeItem.removeItems();

				if (selectGrid != null && parentTreeItem.equals(ftpTree.getSelectedItem()))
					selectGrid.clear();
				
				while (it.hasNext()) {
					FTPFileItem ftpFileItem = (FTPFileItem) it.next();
					
					if (ftpFileItem.getType().equals("d")) {
						/*if (!ftpFileItem.getName().equals(".") && !ftpFileItem.getName().equals("..")) {
							String[] test = ftpFileItem.getName().split("\\.");
							if (test.length > 1) {
								ftpTreeItem = new FTPTreeItem(ftpFileItem.getName());
								System.out.println();
							} else {
								ftpTreeItem = new FTPTreeItem(ftpFileItem.getName(), parentTreeItem);
							}
							parentTreeItem.addItem(ftpTreeItem);
						}*/
						ftpTreeItem = new FTPTreeItem(ftpFileItem);
						parentTreeItem.addItem(ftpTreeItem);
					}
					else {
						ftpTreeItem = new FTPTreeItem(ftpFileItem);
						ftpTreeItem.setVisible(false);
						parentTreeItem.addItem(ftpTreeItem);
						
						if (selectGrid != null && parentTreeItem.equals(ftpTree.getSelectedItem()))
							selectGrid.addItem((FTPFileItem) ftpFileItem);
					}
				}
			} else {
			}
		}
	}

	public void addTreeListener(TreeListener listener) {
		ftpTree.addTreeListener(listener);
	}

	public void removeTreeListener(TreeListener listener) {
		ftpTree.removeTreeListener(listener);
	}

}
