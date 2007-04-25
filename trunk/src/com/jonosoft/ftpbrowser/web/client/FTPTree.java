/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;

/**
 * @author Jkelling
 * 
 */
public class FTPTree extends Composite implements TreeListener {
	private final CCTree ftpTree = new CCTree();
	//private final VerticalPanel myPan = new VerticalPanel();
	private FTPSite ftpConnection = null;
	// private String fileExtension[];
	private ArrayList myList;
	private FTPTreeListenerCollection listeners = null;

	public FTPTree() {
		initWidget(ftpTree);
		
		addStyleName("gwt-ftptree");
		
		myList = new ArrayList();
		myList.add("php");
		myList.add("css");
		
		ftpTree.addTreeListener(this);
	}
	
	public void setFTPConnection(FTPSite ftpConnection) {
		this.ftpConnection = ftpConnection;
		resetConnection();
	}
	
	private void resetConnection() {
		ftpTree.clear();
		final FTPTreeItem ftpTreeItem = new FTPTreeItem(new FTPFileItem(ftpConnection.getFtpSiteId() , "/", "d"), "/" + ftpConnection.getHost());
		ftpTreeItem.setData();
		ftpTreeItem.setNeedsToLoad(false);
		ftpTree.addItem(ftpTreeItem);
		
		//ftpConnection.getList("/", new FTPGetDirectoryContentsResponseHandler((HasTreeItems) ftpTree));
		fetchContents(ftpTreeItem);
		DeferredCommand.add(new Command() {
			// When setFTPConnection is called from the constructor isAttached() won't be true right away
			public void execute() {
				if (isAttached())
					ftpTreeItem.setState(true);
			}
		});
	}
	
	private void fetchContents(final FTPTreeItem parentTreeItem) {
		FTPConnectionController.getList(ftpConnection, parentTreeItem.getFTPFileItem().getFullPath(), new FTPAsyncCallback() {
			public void onFailure(Throwable caught) {
				try {
					throw caught;
				} catch (CCFtpLoginException e) {
					Window.alert("Login failed for site:\n\n" + ftpConnection.toString());
					ftpTree.clear();
				} catch (Throwable ignoredException) {
					Window.alert(ignoredException.getMessage());
				}
			}

			public void onSuccess(Object result) {
				FTPTreeItem ftpTreeItem = null;
				
				if (result instanceof List) {
					final List ar = (List) result;
					final Iterator it = ar.iterator();
					
					parentTreeItem.removeItems();
					
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
						}
					}
				} else {
				}

				if (listeners != null)
					listeners.fireAfterDataReceived(parentTreeItem);
			}
		});
	}
	
	public TreeItem getSelectedItem() {
		return ftpTree.getSelectedItem();
	}
	
	/*public void extenList(ArrayList iList) {
		myList = iList;
	}*/

	public void onTreeItemSelected(TreeItem item) {
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
		if (! item.hasData() && item.getNeedsToLoad()) {
			// if item.getNeedsToLoad() is true, then there should be a temporary child
			// TreeItem that needs to be removed, but it's safe to remove them all anyway
			//item.removeItems();
			DeferredCommand.add(new Command() {
				public void execute() {
					// Using a DeferredCommand to execute this makes for a
					// snappier UI; oherwise, there would be a short delay
					// after clicking before the items appears selected.
					fetchContents(item);
				}
			});
			item.setData();
			item.setNeedsToLoad(false);
		}
		
		if (listeners != null)
			listeners.fireItemSelected(item);
	}

	/*public VerticalPanel myPanel() {
		return myPan;
	}*/

	public void onTreeItemStateChanged(TreeItem item) {
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
						fetchContents(item);
					}
				});
				item.setData();
				item.setNeedsToLoad(false);
			}
			ftpTree.setSelectedItem(item);
		}

		if (listeners != null)
			listeners.fireItemSelected(item);
	}

	public void addTreeListener(FTPTreeListener listener) {
		if (listeners == null) {
			listeners = new FTPTreeListenerCollection();
		}
		listeners.add(listener);
	}

	public void removeTreeListener(FTPTreeListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}
	}
	
	private class FTPTreeListenerCollection extends Vector {
		private static final long serialVersionUID = 5521187734427175581L;

		public void fireItemSelected(TreeItem item) {
			for (Iterator it = iterator(); it.hasNext();) {
				TreeListener listener = (TreeListener) it.next();
				listener.onTreeItemSelected(item);
			}
		}

		public void fireItemStateChanged(TreeItem item) {
			for (Iterator it = iterator(); it.hasNext();) {
				TreeListener listener = (TreeListener) it.next();
				listener.onTreeItemStateChanged(item);
			}
		}
		
		public void fireAfterDataReceived(FTPTreeItem parentTreeItem) {
			for (Iterator it = iterator(); it.hasNext();) {
				FTPTreeListener listener = (FTPTreeListener) it.next();
				listener.afterDataReceived(parentTreeItem);
			}
		}
	}

}
