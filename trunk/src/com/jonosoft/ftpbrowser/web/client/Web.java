/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.Collection;
import java.util.Iterator;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;
/**
 * @author Jkelling
 *
 */
public class Web implements EntryPoint {
	private final HorizontalPanel directoryBrowserPanel = new HorizontalPanel();
	private final VerticalPanel vertPanel = new VerticalPanel();
	private static final FTPFileItemSelectGrid selectGrid = new FTPFileItemSelectGrid();
	private static final FTPTree myTree = new FTPTree();
	private final FTPFileItemSelectGrid savedItemGrid = new FTPFileItemSelectGrid();
	private final TextBox tb = new TextBox();
	private final Label myLabel = new Label();
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
		
		directoryBrowserPanel.add(new FTPConnectionsMenuBar());
		directoryBrowserPanel.add(myTree);
		directoryBrowserPanel.add(myTree.myPanel());
		directoryBrowserPanel.setBorderWidth(10);

		directoryBrowserPanel.add(selectGrid);
		
		selectGrid.addItemSelectGridListener(new Responder());

		/*selectGrid.setText(0, 0, "Stuff");
		selectGrid.setText(1, 0, "Stuff");
		selectGrid.setText(2, 0, "Stuff");
		selectGrid.setText(3, 0, "Stuff");
		selectGrid.setText(4, 0, "Stuff");
		selectGrid.setText(5, 0, "Stuff");
		selectGrid.setText(6, 0, "Stuff");
		selectGrid.setText(7, 0, "Stuff");
		selectGrid.setText(8, 0, "Stuff");
		selectGrid.setText(9, 0, "Stuff");
		selectGrid.setText(10, 0, "Stuff");
		selectGrid.setText(11, 0, "Stuff");
		selectGrid.setText(12, 0, "Stuff");
		selectGrid.setText(13, 0, "Stuff");
		selectGrid.setText(14, 0, "Stuff");

		selectGrid.setText(0, 1, "Stuff");
		selectGrid.setText(1, 1, "Stuff");
		selectGrid.setText(2, 1, "Stuff");
		selectGrid.setText(3, 1, "Stuff");
		selectGrid.setText(4, 1, "Stuff");
		selectGrid.setText(5, 1, "Stuff");
		selectGrid.setText(6, 1, "Stuff");
		selectGrid.setText(7, 1, "Stuff");
		selectGrid.setText(8, 1, "Stuff");
		selectGrid.setText(9, 1, "Stuff");
		selectGrid.setText(10, 1, "Stuff");
		selectGrid.setText(11, 1, "Stuff");
		selectGrid.setText(12, 1, "Stuff");
		selectGrid.setText(13, 1, "Stuff");
		selectGrid.setText(14, 1, "Stuff");*/
		
		vertPanel.add(directoryBrowserPanel);
		vertPanel.add(savedItemGrid);

		RootPanel.get().add(new FTPBrowser());
		RootPanel.get().add(vertPanel);
		
		RootPanel.get().add(new Button("Clear", new ClickListener() {
			public void onClick(Widget sender) {
				selectGrid.clear();
			}
		}));
	}
	
	public static FTPFileItemSelectGrid getFTPFileItemSelectGrid() {
		return selectGrid;
	}
	
	public static FTPTree getFTPTree() {
		return myTree;
	}
	
	private class Responder implements ItemSelectGridListener {
		public void onItemsSelectStateChanged(ItemSelectGrid sender, Collection items, boolean state) {
			if (! state) {
				for (Iterator it = items.iterator(); it.hasNext();)
					savedItemGrid.removeItem((FTPFileItem) it.next());
			}
			else {
				for (Iterator it = items.iterator(); it.hasNext();)
					savedItemGrid.addItem((FTPFileItem) it.next());
			}
		}
	}

}
