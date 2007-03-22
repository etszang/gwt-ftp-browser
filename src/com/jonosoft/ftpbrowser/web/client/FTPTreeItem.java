package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.user.client.ui.TreeItem;



public class FTPTreeItem extends TreeItem {
	private String path = null;
	private boolean hasData = false;
	private boolean isFile = false;
	public FTPTreeItem(String path) {
		setPath(path);
		setText(path);
		setData();
		isFile = true;
	}
	public String FTPItemName(){
		return path;
	}
	public FTPTreeItem(){
		
	}
	public FTPTreeItem(String path, FTPTreeItem parent) {
		//TODO Should strip "/" from the end of parent.getPath() if it exists
		setPath(parent.getPath() + "/" + path);
		setText(path);
	}
	
	public String getPath() {
		return this.path;
	}
	public boolean isFile(){
		return isFile;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public void setData(){
		hasData=true;
	}
	public boolean hasData(){
		return hasData;
	}
}