/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.Collection;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * @author Jkelling
 *
 */
public class Web implements EntryPoint {
	private final VerticalPanel vertPanel = new VerticalPanel();
	private static final FTPBrowser ftpBrowser = new FTPBrowser();
    //private CheckFile myCheck ;
	
	/**
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	public void onModuleLoad() {
		/*
		 * Commented out the following 3/24/07 because I didn't have the CheckFile class
		 */
		/*myCheck = new CheckFile(myTree.myPanel());
		myTree.extenList(myCheck.getList());
		directoryBrowserPanel.add(myCheck);*/
		
		//selectGrid.addItemSelectGridListener(new Responder());
		
		vertPanel.add(ftpBrowser);

		RootPanel.get("gwt-content").add(vertPanel);
	}
	
	public static FTPBrowser getFTPBrowser() {
		return ftpBrowser;
	}
	
	private class Responder implements ItemSelectGridListener {
		public void onItemsSelectStateChanged(ItemSelectGrid sender, Collection items, boolean state) {/*
			if (! state) {
				for (Iterator it = items.iterator(); it.hasNext();)
					savedItemGrid.removeItem((FTPFileItem) it.next());
			}
			else {
				for (Iterator it = items.iterator(); it.hasNext();)
					savedItemGrid.addItem((FTPFileItem) it.next());
			}
		*/}
	}

}
