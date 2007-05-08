/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.Iterator;


/**
 * @author Jkelling
 *
 */
public class FTPFileItemSelectGrid extends ItemSelectGrid {
	
	public static final int READ = 1;
	public static final int WRITE = 2;
	
	private boolean displayFullPaths = false;
	private String filePatternsToDisplay = null;
	private int userPermissions = READ | WRITE;
	
	public FTPFileItemSelectGrid() {
		super(0, 1);
		
		addStyleName("ftp-itemselectgrid");
	}

	/**
	 * @see com.jonosoft.ftpbrowser.web.client.ItemSelectGrid#buildItemRow(int, java.lang.Object)
	 */
	public void buildItemRow(int rowIndex, Object item) {
		if (isDisplayFullPaths()) {
			setText(rowIndex, 0, ((FTPFileItem) item).getFullPath());
		}
		else {
			FTPFileItem fileItem = (FTPFileItem) item;
			if ("..".equals(fileItem.getName()))
				setHTML(rowIndex, 0, ".. <i>(parent)</i>");
			else
				setText(rowIndex, 0, fileItem.getName());
		}
		getCellFormatter().addStyleName(rowIndex, 0, "ftp-itemselectgrid");
	}
	
	public String getFilePatternsToDisplay() {
		return filePatternsToDisplay;
	}
	
	public void setFilePatternsToDisplay(String filePatternsToDisplay) {
		this.filePatternsToDisplay = filePatternsToDisplay;
		
		for (Iterator it = getItems().iterator(); it.hasNext();)
			setItemHiddenState((FTPFileItem) it.next());
	}
	
	private void setItemHiddenState(FTPFileItem fileItem) {
		if (filePatternsToDisplay == null) {
			setItemIsHidden(fileItem, false);
			return;
		}
		
		String[] filePatterns = filePatternsToDisplay.split("\\s+");
		
		for (int i = 0; i < filePatterns.length; i++)
			if (fileItem.getName().endsWith(filePatterns[i])) {
				setItemIsHidden(fileItem, false);
				return;
			}
		setItemIsHidden(fileItem, true);
	}
	
	private void setItemIsHidden(FTPFileItem fileItem, boolean state) {
		if (state)
			getCellFormatter().addStyleName(getRowIndexByItem(fileItem), 0, "ftp-itemselectgrid-item-hidden");
		else
			getCellFormatter().removeStyleName(getRowIndexByItem(fileItem), 0, "ftp-itemselectgrid-item-hidden");
	}
	
	private boolean isItemHidden(FTPFileItem fileItem) {
		if (filePatternsToDisplay == null) {
			return false;
		}
		
		String[] filePatterns = filePatternsToDisplay.split("\\s+");
		
		for (int i = 0; i < filePatterns.length; i++)
			if (fileItem.getName().endsWith(filePatterns[i])) {
				return false;
			}
		return true;
	}

	public boolean isDisplayFullPaths() {
		return displayFullPaths;
	}
	
	public void setDisplayFullPaths(boolean displayFullPaths) {
		this.displayFullPaths = displayFullPaths;
		refreshDisplay();
	}
	
	public int getUserPermissions() {
		return userPermissions;
	}
	
	public void setUserPermissions(int bits) {
		userPermissions = bits;
	}
	
	public boolean canRead() {
		return hasPermission(READ);
	}
	
	public boolean canWrite() {
		return hasPermission(WRITE);
	}
	
	private final boolean hasPermission(final int bit) {
		return (userPermissions & bit) > 0;
	}
	
	public void addItem(Object item) {
		if (item == null)
			return;
		
		if (isItemHidden((FTPFileItem) item))
			return;
		
		FTPFileItem fileItem = (FTPFileItem) item;
		
		if ("/..".equals(fileItem.getFullPath()) || ".".equals(fileItem.getName()))
			return;
		
		super.addItem(item);
		
		int rowIndex = getRowIndexByItem(item);
		getRowFormatter().addStyleName(rowIndex, "ftp-itemselectgrid-item");
		getRowFormatter().addStyleName(rowIndex, "ftp-itemselectgrid-item-" + fileItem.getType());
		
		/*if ("f".equals(fileItem.getType()) && fileItem.getName().indexOf('.') != -1 && ! fileItem.getName().endsWith(".")) {
			String fileExtension = fileItem.getName().substring(fileItem.getName().lastIndexOf('.')+1);
			getRowFormatter().addStyleName(rowIndex, "ftp-itemselectgrid-item-f-" + fileExtension);
			setItemHiddenState(fileItem);
		}*/
	}
	
}