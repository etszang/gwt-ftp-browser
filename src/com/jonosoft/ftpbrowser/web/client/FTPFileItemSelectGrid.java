/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;


/**
 * @author Jkelling
 *
 */
public class FTPFileItemSelectGrid extends ItemSelectGrid {
	
	private boolean displayFullPaths = false;
	
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
	
	public boolean isDisplayFullPaths() {
		return displayFullPaths;
	}
	
	public void setDisplayFullPaths(boolean displayFullPaths) {
		this.displayFullPaths = displayFullPaths;
		refreshDisplay();
	}
	
	public void addItem(Object item) {
		if (item == null)
			return;
		
		FTPFileItem fileItem = (FTPFileItem) item;
		
		if ("/..".equals(fileItem.getFullPath()) || ".".equals(fileItem.getName()))
			return;
		
		super.addItem(item);
		
		int rowIndex = getRowIndexByItem(item);
		getRowFormatter().addStyleName(rowIndex, "ftp-itemselectgrid-item");
		getRowFormatter().addStyleName(rowIndex, "ftp-itemselectgrid-item-" + fileItem.getType());
	}
	
}