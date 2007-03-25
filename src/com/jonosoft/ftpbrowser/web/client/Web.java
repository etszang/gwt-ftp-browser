/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.*;
/**
 * @author Jkelling
 *
 */
public class Web implements EntryPoint {
	private final HorizontalPanel directoryBrowserPanel = new HorizontalPanel();
	private final FTPTree myTree = new FTPTree();
	private final TextBox tb = new TextBox();
	private final Label myLabel = new Label();
    //private CheckFile myCheck ;
	
	/* (non-Javadoc)
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	public void onModuleLoad() {
	
		/*
		 * Commented out the following 3/24/07 because I didn't have the CheckFile class
		 */
		/*myCheck = new CheckFile(myTree.myPanel());
		myTree.extenList(myCheck.getList());
		directoryBrowserPanel.add(myCheck);*/
		directoryBrowserPanel.add(new FTPConnectionsMenuBar());
		 directoryBrowserPanel.add(myTree);
		 directoryBrowserPanel.add(myTree.myPanel());
		 directoryBrowserPanel.setBorderWidth(10);
		 
		 
		
		 RootPanel.get().add(new FTPBrowser());
		 RootPanel.get().add(directoryBrowserPanel);
		 

		
	}

}
