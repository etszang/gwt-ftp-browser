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
		if (isDisplayFullPaths())
			setText(rowIndex, 0, ((FTPFileItem) item).getFullPath());
		else
			setText(rowIndex, 0, ((FTPFileItem) item).getName());
	}
	
	public boolean isDisplayFullPaths() {
		return displayFullPaths;
	}
	
	public void setDisplayFullPaths(boolean displayFullPaths) {
		this.displayFullPaths = displayFullPaths;
		refreshDisplay();
	}
	
}
