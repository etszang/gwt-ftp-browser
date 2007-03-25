/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.*;
import com.google.gwt.user.client.ui.*;

/**
 * @author Jkelling
 * 
 */
public class FTPTree extends Composite implements TreeListener {
	private final Tree ftpTree = new Tree();
	private final VerticalPanel myPan = new VerticalPanel();
	private final Tree tempTree = new Tree();
	private FTPConnection ftpConnection = null;
	// private String fileExtension[];
	private ArrayList myList;

	public FTPTree() {
		initWidget(ftpTree);
		
		FTPConnection ftpConnection = new FTPConnection();
		ftpConnection.setServer("cookiecloaker.com");
		ftpConnection.setPort(21);
		ftpConnection.setUsername("u39984585-cookies");
		ftpConnection.setPassword("haberman");
		
		ftpTree.addTreeListener(this);
		tempTree.addTreeListener(this);
		
		setFTPConnection(ftpConnection);
	}
	
	public void setFTPConnection(FTPConnection ftpConnection) {
		this.ftpConnection = ftpConnection;
		ftpTree.clear();

		FTPTreeItem rootItem = new FTPTreeItem("/");
		rootItem.setState(true);
		
		ftpTree.addItem(rootItem);
		
		ftpConnection.getList(rootItem.getPath(), new FTPGetDirectoryContentsResponseHandler(rootItem));
		
		rootItem.setData();
	}

	public void setRootPath() {
		ftpTree.addTreeListener(this);
	}

	public void extenList(ArrayList iList) {
		myList = iList;
	}

	public void onTreeItemSelected(TreeItem item) {
		final FTPTreeItem ftpItem = (FTPTreeItem) item;
		if (ftpItem.hasData() == false) {
			ftpConnection.getList(ftpItem.getPath(), new FTPGetDirectoryContentsResponseHandler(ftpItem));
			ftpItem.setData();
		}
		if (myPan != null) {
			myPan.clear();
		}
		for (int i = 0; i < ftpItem.getChildCount(); i++) {
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
		}
	}

	public VerticalPanel myPanel() {
		return myPan;
	}

	public void onTreeItemStateChanged(TreeItem item) {
		if (item instanceof FTPTreeItem) {
			// onFTPTreeItemStateChanged((FTPTreeItem) item);
		}
	}

	private void onFTPTreeItemStateChanged(FTPTreeItem item) {
		if (item.getState()) {
			// ftpConnection.getList(item.getPath(), new
			// FTPGetDirectoryContentsResponseHandler(item));
		}
	}

	protected class FTPGetDirectoryContentsResponseHandler implements
			FTPAsyncCallback {
		private FTPTreeItem parentTreeItem = null;

		public FTPGetDirectoryContentsResponseHandler(FTPTreeItem parentTreeItem) {
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
				final Iterator i = ar.iterator();
				
				while (i.hasNext()) {
					FTPFileItem ftpFileItem = (FTPFileItem) i.next();
					
					if (ftpFileItem.getType().equals("d")) {
						if (!ftpFileItem.getName().equals(".") && !ftpFileItem.getName().equals("..")) {
							String[] test = ftpFileItem.getName().split("\\.");
							if (test.length > 1) {
								ftpTreeItem = new FTPTreeItem(ftpFileItem.getName());
								System.out.println();
							} else {
								ftpTreeItem = new FTPTreeItem(ftpFileItem.getName(), parentTreeItem);
							}
							parentTreeItem.addItem(ftpTreeItem);
						}
					}
				}
			} else {

			}
		}
	}
}
