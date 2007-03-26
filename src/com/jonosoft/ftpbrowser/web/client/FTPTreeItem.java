package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.user.client.ui.TreeItem;

public class FTPTreeItem extends TreeItem {
	private String path = null;
	private boolean hasData = false;
	private boolean isFile = false;
	private boolean needsToLoad = true;
	
	public FTPTreeItem(String path) {
		setPath(path);
		setText(path);
		//isFile = true;
		
		setupTempSubItem();
	}
	
	public FTPTreeItem(String path, FTPTreeItem parent) {
		// TODO Should strip "/" from the end of parent.getPath() if it exists
		if (parent.getPath().endsWith("/"))
			setPath(parent.getPath() + path);
		else
			setPath(parent.getPath() + "/" + path);
		setText(path);
		//isFile = true;
		
		setupTempSubItem();
	}

	private void setupTempSubItem() {
		setNeedsToLoad(true);
		addItem("loading...");
	}

	public String FTPItemName() {
		return path;
	}

	public String getPath() {
		return this.path;
	}

	public boolean isFile() {
		return isFile;
	}

	public void setPath(String path) {
		this.path = path;
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
}