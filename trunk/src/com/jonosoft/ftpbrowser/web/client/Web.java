/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * @author Jkelling
 *
 */
public class Web implements EntryPoint {
	private final VerticalPanel vertPanel = new VerticalPanel();
	private final FTPBrowser ftpBrowser = new FTPBrowser();
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
		
		if (RootPanel.get("gwt-content") != null)
			RootPanel.get("gwt-content").add(vertPanel);
		
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable e) {
				GWTErrorLogger.logError(e);
			}
		});
	}

}
