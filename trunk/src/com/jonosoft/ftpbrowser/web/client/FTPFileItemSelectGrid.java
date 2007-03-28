/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

/**
 * @author Jkelling
 *
 */
public class FTPFileItemSelectGrid extends ItemSelectGrid {
	
	public FTPFileItemSelectGrid() {
		super(0, 1);
		
		addStyleName("ftp-itemselectgrid");
	}

	/**
	 * @see com.jonosoft.ftpbrowser.web.client.ItemSelectGrid#buildItemRow(int, java.lang.Object)
	 */
	public void buildItemRow(int rowIndex, Object item) {
		setText(rowIndex, 0, ((FTPFileItem) item).getName());
	}
	
}
