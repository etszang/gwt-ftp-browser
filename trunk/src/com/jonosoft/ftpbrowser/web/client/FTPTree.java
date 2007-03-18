/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;

/**
 * @author Jkelling
 *
 */
public class FTPTree extends Composite implements TreeListener {
	private final Tree ftpTree = new Tree();
	private FTPConnection ftpConnection = null;
	
	public FTPTree() {
		initWidget(ftpTree);
		
		ftpConnection = new FTPConnection();
		
		ftpConnection.setServer("cookiecloaker.com");
		ftpConnection.setPort(21);
		ftpConnection.setUsername("u39984585-cookies");
		ftpConnection.setPassword("haberman");
		
		ftpTree.addTreeListener(this);
		
		FTPTreeItem rootItem = new FTPTreeItem("/");
		ftpTree.addItem(rootItem);
		rootItem.setState(true);
		ftpConnection.getList(rootItem.getPath(), new FTPGetDirectoryContentsResponseHandler(rootItem));
	}
	
	public void setRootPath() {
		ftpTree.addTreeListener(this);
	}

	public void onTreeItemSelected(TreeItem item) {
		FTPTreeItem ftpItem = (FTPTreeItem) item;
		ftpConnection.getList(ftpItem.getPath(), new FTPGetDirectoryContentsResponseHandler(ftpItem));
	}

	public void onTreeItemStateChanged(TreeItem item) {
		if (item instanceof FTPTreeItem) {
			onFTPTreeItemStateChanged((FTPTreeItem) item);
		}
	}
	
	private void onFTPTreeItemStateChanged(FTPTreeItem item) {
		if (item.getState()) {
			//ftpConnection.getList(item.getPath(), new FTPGetDirectoryContentsResponseHandler(item));
		}
	}
	
	protected class FTPGetDirectoryContentsResponseHandler implements FTPAsyncCallback {
		private FTPTreeItem parentTreeItem = null;
		
		public FTPGetDirectoryContentsResponseHandler(FTPTreeItem parentTreeItem) {
			this.parentTreeItem = parentTreeItem;
		}
		
		public void onFailure(Throwable caught) {
			try {
				throw caught;
			}
			catch (Throwable ignoredException) {
			}
		}

		public void onSuccess(Object result) {
			FTPTreeItem ftpTreeItem = null;
			if (result instanceof List) {
				final List ar = (List) result;
				final Iterator i = ar.iterator();
				while (i.hasNext()) {
					String dir = (String) i.next();
					ftpTreeItem = new FTPTreeItem(dir, parentTreeItem);
					parentTreeItem.addItem(ftpTreeItem);
				}
			}
			else {
				//throw new RuntimeException("Unsupported result type");
			}
		}
	}
}
