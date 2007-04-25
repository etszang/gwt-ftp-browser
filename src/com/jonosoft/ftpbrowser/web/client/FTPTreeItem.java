package com.jonosoft.ftpbrowser.web.client;

import java.util.List;
import java.util.Vector;

import com.google.gwt.user.client.ui.TreeItem;

public class FTPTreeItem extends CCTreeItem {
	private FTPFileItem ftpFileItem = null;
	private boolean hasData = false;
	private boolean needsToLoad = true;
	private final List fileItems = new Vector();

	public FTPTreeItem(FTPFileItem ftpFileItem) {
		super();
		
		setFTPFileItem(ftpFileItem);
		setText(ftpFileItem.getName());
		
		if (ftpFileItem.getType().equals("d"))
			setupTempSubItem();
		
		addStyleName("gwt-ftptreeitem");
		addStyleName("gwt-ftptreeitem-" + getFTPFileItem().getType());
	}
	
	public FTPTreeItem(FTPFileItem ftpFileItem, String displayText) {
		this(ftpFileItem);
		setText(displayText);
	}
	
	private void setupTempSubItem() {
		setNeedsToLoad(true);
		addItem("loading...");
	}

	public FTPFileItem getFTPFileItem() {
		return this.ftpFileItem;
	}

	public void setFTPFileItem(FTPFileItem ftpFileItem) {
		this.ftpFileItem = ftpFileItem;
	}

	public void setData() {
		hasData = true;
	}

	public boolean hasData() {
		return hasData;
	}
	
	/**
	 * I think this is the same as hasData() but I didn't know that until after
	 * I made it. This can be refactored later.
	 */
	public boolean getNeedsToLoad() {
		return needsToLoad;
	}
	
	public void setNeedsToLoad(boolean needsToLoad) {
		this.needsToLoad = needsToLoad;
	}
	
	public List getFileItems() {
		return fileItems;
	}
	
	/**
	 * This is sort of a hack, just so that 
	 * 
	 * @see TreeItem#addItem(TreeItem)
	 */
	public void addItem(TreeItem item) {
		if (item instanceof FTPTreeItem) {
			FTPTreeItem ftpTreeItem = (FTPTreeItem) item;
			if (ftpTreeItem.getFTPFileItem().getType().equals("f")) {
				fileItems.add(ftpTreeItem);
				return;
			}
			super.addItem(ftpTreeItem);
		}
		super.addItem(item);
	}
	
	/**
	 * @see TreeItem#removeItem(TreeItem)
	 */
	public void removeItem(CCTreeItem item) {
		fileItems.remove(item);
		super.removeItem(item);
	}
}