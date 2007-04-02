/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.HTTPRequest;
import com.google.gwt.user.client.ResponseTextHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jkelling
 *
 */
public class FTPBrowser extends Composite {
	private static final String LINE_SEPARATOR = String.valueOf((char)13);
	
	private final VerticalPanel vertPanel = new VerticalPanel();
	private final HorizontalPanel directoryBrowserPanel = new HorizontalPanel();
	/*private final Button parentLevelButton = new Button("Go up one directory");
	private final Button openDirectoryButton = new Button("Open selection");*/
	private final SelectionChangedListenerCollection listenerCollection = new SelectionChangedListenerCollection();

	private final FTPFileItemSelectGrid ftpFileSelectGrid = new FTPFileItemSelectGrid();
	private final FTPConnectionsMenuBar ftpConnectionsMenuBar = new FTPConnectionsMenuBar();
	private FTPTree ftpTree = null;
	private final FTPFileItemSelectGrid savedItemGrid = new FTPFileItemSelectGrid();

	public FTPBrowser() {
		initWidget(vertPanel);

		addStyleName("gwt-ftpbrowser");

		ScrollPanel sc2 = new ScrollPanel(ftpFileSelectGrid);
		ScrollPanel sc1 = new ScrollPanel(ftpTree = new FTPTree());

		sc1.setPixelSize(350, 300);
		sc2.setPixelSize(200, 300);
		
		vertPanel.add(directoryBrowserPanel);
		vertPanel.add(savedItemGrid);

		directoryBrowserPanel.add(ftpConnectionsMenuBar);
		directoryBrowserPanel.add(sc1);
		directoryBrowserPanel.add(sc2);

		//directoryBrowserPanel.add(parentLevelButton);
		//directoryBrowserPanel.add(directoryListPanel);
		//directoryBrowserPanel.add(openDirectoryButton);

		/*parentLevelButton.addClickListener(this);
		openDirectoryButton.addClickListener(this);*/

		ftpTree.addTreeListener(new FTPTreeListener());
		ftpFileSelectGrid.addItemSelectGridListener(new Responder());
	}

	public void changeDirectory(String path) {

	}

	public void addSelectionChangedListener(SelectionChangedListener listener) {
		listenerCollection.add(listener);
	}

	public void onClick(Widget sender) {

	}

	public FTPConnectionsMenuBar getFTPConnectionsMenuBar() {
		return ftpConnectionsMenuBar;
	}

	public FTPTree getFTPTree() {
		return ftpTree;
	}

	public FTPFileItemSelectGrid getFTPFileItemSelectGrid() {
		return ftpFileSelectGrid;
	}

	private class FTPTreeListener implements TreeListener {

		public void onTreeItemSelected(TreeItem item) {
			
		}

		public void onTreeItemStateChanged(TreeItem item) {
			// TODO Auto-generated method stub

		}

	}
	
	private class Responder implements ItemSelectGridListener {
		public void onItemsSelectStateChanged(ItemSelectGrid sender, List items, boolean state) {
			if (! state) {
				for (Iterator it = items.iterator(); it.hasNext();)
					savedItemGrid.removeItem((FTPFileItem) it.next());
			}
			else {
				for (Iterator it = items.iterator(); it.hasNext();)
					savedItemGrid.addItem((FTPFileItem) it.next());
			}
			saveSavedItems();
		}
	}
	
	private void saveSavedItems() {
		Map paramStringBySiteId = new HashMap();
		Iterator it = savedItemGrid.getItems().iterator();
		List paramArray = new Vector();
		StringBuffer sb = null;
		String pathsAsBase64 = null;
		Integer ftpSiteId = null;
		FTPFileItem ftpFileItem = null;
		
		while (it.hasNext()) {
			ftpFileItem = (FTPFileItem) it.next();
			ftpSiteId = new Integer(ftpFileItem.getFTPConnection().getFtpSiteId());
			
			if ((sb = (StringBuffer) paramStringBySiteId.get(ftpSiteId)) == null)
				paramStringBySiteId.put(ftpSiteId, sb = new StringBuffer());
			
			if (sb.length() != 0)
				sb.append(LINE_SEPARATOR);
			
			sb.append(ftpFileItem.getType() + ":" + ftpFileItem.getFullPath());
		}
		
		for (it = paramStringBySiteId.keySet().iterator(); it.hasNext();) {
			ftpSiteId = (Integer) it.next();
			sb = (StringBuffer) paramStringBySiteId.get(ftpSiteId);
			pathsAsBase64 = Base64Util.encode(sb.toString());
			paramArray.add(ftpSiteId.toString());
			paramArray.add(pathsAsBase64);
		}
		
		System.out.println(getURLQueryParamsAsString(paramArray));
		
		HTTPRequest.asyncPost(GWT.getModuleBaseURL()+CookieCloaker.DEFAULT_INSTANCE.ftpPathSaveURL(), getURLQueryParamsAsString(paramArray), new ResponseTextHandler() {
			public void onCompletion(String responseText) {
				System.out.println(responseText);
			}
		});
	}
	
	private static String getURLQueryParamsAsString(final List paramArray) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < paramArray.size(); i+=2) {
			if (sb.length() > 0)
				sb.append("&");
			sb.append(paramArray.get(i) + "=" + paramArray.get(i+1));
		}
		return sb.toString();
	}

}
