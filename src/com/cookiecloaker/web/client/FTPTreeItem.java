package com.cookiecloaker.web.client;

import com.google.gwt.user.client.ui.TreeItem;



public class FTPTreeItem extends TreeItem {
	private String path = null;
	
	public FTPTreeItem(String path) {
		setPath(path);
		setText(path);
	}
	
	public FTPTreeItem(String path, FTPTreeItem parent) {
		//TODO Should strip "/" from the end of parent.getPath() if it exists
		setPath(parent.getPath() + "/" + path);
		setText(path);
	}
	
	public String getPath() {
		return this.path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
}