package com.jonosoft.ftpbrowser.web.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.HTTPRequest;
import com.google.gwt.user.client.ResponseTextHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;

/**
 * @author Jkelling
 */
public class FTPFileGroupWidget extends Composite {
	private static final String LINE_SEPARATOR = String.valueOf((char)13);
	private static final int SAVE_DELAY = 10000;
	
	private final FTPFileItemSelectGrid fileGrid = new FTPFileItemSelectGrid();
	private final FileSaver fileSaver = new FileSaver();
	private FTPFileItemSelectGrid sourceFileGrid = null;
	private final FDAS fdsa = new FDAS();
	
	public FTPFileGroupWidget(FTPFileItemSelectGrid sourceFileGrid) {
		initWidget(fileGrid);
		
		setSourceFileGrid(sourceFileGrid);
		
		fileGrid.setDisplayFullPaths(true);
		
		addStyleName("cc-filegroupwidget");
		
		DeferredCommand.add(new Command() {
			public void execute() {
				new FTPFileGroupFilesDataProvider().load();
			}
		});
	}
	
	private FTPFileItemSelectGrid getSourceFileGrid() {
		return sourceFileGrid;
	}
	
	private void setSourceFileGrid(final FTPFileItemSelectGrid sourceFileGrid) {
		if (this.sourceFileGrid != null && this.sourceFileGrid != sourceFileGrid)
			this.sourceFileGrid.removeItemSelectGridListener(fdsa);
		this.sourceFileGrid = sourceFileGrid;
		this.sourceFileGrid.addItemSelectGridListener(fdsa);
	}
	
	private boolean containsFTPFileItem(FTPFileItem itemToFind) {
		return fileGrid.getItems().contains(itemToFind);
		/*for (Iterator it = fileGrid.getItems().iterator(); it.hasNext();) {
			FTPFileItem ftpFileItem = (FTPFileItem) it.next();
			if (ftpFileItem.equals(itemToFind))
				return true;
		}
		return false;*/
	}
	
	private class FDAS implements ItemSelectGridListener {
		public void onItemsSelectStateChanged(ItemSelectGrid sender, List items, boolean state) {
			if (! state) {
				for (Iterator it = items.iterator(); it.hasNext();)
					fileGrid.removeItem((FTPFileItem) it.next());
			}
			else {
				for (Iterator it = items.iterator(); it.hasNext();)
					fileGrid.addItem((FTPFileItem) it.next());
			}
			
			fileSaver.save();
		}

		public void onItemAdded(ItemSelectGrid sender, Object item) {
			sender.setItemSelected(item, containsFTPFileItem((FTPFileItem) item));
		}
	}
	
	private class FTPFileGroupFilesDataProvider {
		public void load() {
			HTTPRequest.asyncGet(GWT.getModuleBaseURL()+CookieCloaker.DEFAULT_INSTANCE.ftpPathListURL(), new ResponseTextHandler() {
				public void onCompletion(String responseText) {
					fileGrid.clear();
					
					try {
						JSONResponse jsonResponse = JSONResponse.newInstance(responseText);
						jsonResponse.handleErrors();
						
						JSONArray arPaths = (JSONArray) jsonResponse.getResult().get("pathList");
						
						for (int i = 0; i < arPaths.size(); i++) {
							fileGrid.addItem(FTPFileItem.getInstance((JSONObject) arPaths.get(i)));
						}
					}
					catch (Throwable e) {
						GWTErrorLogger.logError(e);
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	private class FileSaver {
		private boolean isSaveInProgress = false;
		private boolean isSaveBeingDelayed = false;
		private boolean isSaveAfterDelay = false;
		
		public void save() {
			if (isSaveInProgress || isSaveBeingDelayed) {
				isSaveAfterDelay = true;
				return;
			}
			isSaveInProgress = true;
			try {
				saveNow();
			}
			catch (RequestException e) {
				Window.alert("Failed to save FTP file data");
			}
			delaySave();
		}
		
		private void delaySave() {
			if (isSaveBeingDelayed)
				return;
			isSaveBeingDelayed = true;
			new Timer() {
				public void run() {
					isSaveBeingDelayed = false;
					if (isSaveAfterDelay) {
						isSaveAfterDelay = false;
						save();
					}
				}
			}.schedule(SAVE_DELAY);
		}
		
		private void saveNow() throws RequestException {
			Map paramStringBySiteId = new HashMap();
			Iterator it = fileGrid.getItems().iterator();
			StringBuffer sb = null;
			Integer ftpSiteId = null;
			FTPFileItem ftpFileItem = null;
			JSRequestBuilder requestBuilder = new JSRequestBuilder(RequestBuilder.POST, GWT.getModuleBaseURL()+CookieCloaker.DEFAULT_INSTANCE.ftpPathSaveURL());
			
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
				requestBuilder.addParameter(ftpSiteId.toString(), sb.toString());
			}
			
			System.out.println(requestBuilder.getRequestDataAsString());
			
			requestBuilder.setTimeoutMillis(10000);
			requestBuilder.sendRequest(new SaveResponseHandler());
		}
		
		private class SaveResponseHandler implements RequestCallback {
			public void onError(Request request, Throwable exception) {
				GWTErrorLogger.logError(exception);
				Window.alert("Failed to save FTP file data");
			}

			public void onResponseReceived(Request request, Response response) {
				isSaveInProgress = false;
				
				try {
					JSONResponse jsonResponse = JSONResponse.newInstance(response.getText());
					jsonResponse.handleErrors();
				}
				catch (Throwable caught) {
					GWTErrorLogger.logError(caught);
					GWT.log("Saving FTPFileGroup contents failed", caught);
					// TODO Display some kind of error to the user
				}
			}
		}
	}
}