/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * @author Jkelling
 *
 */
public class FTPFileItemSelectGrid extends ItemSelectGrid {
	
	public static final int READ_FILES = 1;
	public static final int WRITE_FILES = 2;
	public static final int COPY_FILES = 4;
	public static final int READ_DIRS = 8;
	public static final int WRITE_DIRS = 16;
	public static final int COPY_DIRS = 32;
	
	private boolean displayFullPaths = false;
	private String filePatternsToDisplay = null;
	private int userPermissions = READ_FILES | WRITE_FILES | COPY_FILES | READ_DIRS | WRITE_DIRS | COPY_DIRS;
	
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
	
	public final boolean canReadFiles() {
		return hasPermission(READ_FILES);
	}
	
	public final boolean canWriteFiles() {
		return hasPermission(WRITE_FILES);
	}
	
	public final boolean canCopyFiles() {
		return hasPermission(COPY_FILES);
	}
	
	public final boolean canReadDirs() {
		return hasPermission(READ_DIRS);
	}
	
	public final boolean canWriteDirs() {
		return hasPermission(WRITE_DIRS);
	}
	
	public final boolean canCopyDirs() {
		return hasPermission(COPY_DIRS);
	}
	
	private final boolean hasPermission(final int bit) {
		return (userPermissions & bit) > 0;
	}
	
	public Set getSelectedFiles() {
		return getSelectedItemsByFTPFileItemType("f");
	}
	
	public Set getSelectedDirs() {
		return getSelectedItemsByFTPFileItemType("d");
	}
	
	private Set getSelectedItemsByFTPFileItemType(final String type) {
		final Set returnItems = new HashSet();
		FTPFileItem fileItem;
		
		for (Iterator it = getSelectedItems().iterator(); it.hasNext();) {
			fileItem = (FTPFileItem) it.next();
			if (type.equals(fileItem.getType()))
				returnItems.add(fileItem);
		}
		
		return returnItems;
	}
	
	public void addItem(final Object item) {
		if (item == null)
			return;
		
		if (((FTPFileItem) item).isFile() && isItemHidden((FTPFileItem) item))
			return;
		
		final FTPFileItem fileItem = (FTPFileItem) item;
		
		if ("/..".equals(fileItem.getFullPath()) || ".".equals(fileItem.getName()))
			return;
		
		super.addItem(item);
		
		int rowIndex = getRowIndexByItem(item);
		getRowFormatter().addStyleName(rowIndex, "ftp-itemselectgrid-item");
		getRowFormatter().addStyleName(rowIndex, "ftp-itemselectgrid-item-" + fileItem.getType());
		
		if (fileItem.isFile() && fileItem.getName().indexOf('.') != -1 && ! fileItem.getName().endsWith(".")) {
			String fileExtension = fileItem.getName().substring(fileItem.getName().lastIndexOf('.')+1);
			getRowFormatter().addStyleName(rowIndex, "ftp-itemselectgrid-item-f-" + fileExtension);
			//setItemHiddenState(fileItem);
		}
	}
	
}