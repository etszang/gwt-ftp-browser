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
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowCloseListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;

/**
 * @author Jkelling
 */
public class FTPFileGroupWidget extends Composite {
	private static final String LINE_SEPARATOR = String.valueOf((char)13);
	
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
	}
	
	// TODO Name this class
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
			fileGrid.sort();
		}

		public void onItemAdded(ItemSelectGrid sender, Object item) {
			sender.setItemSelected(item, containsFTPFileItem((FTPFileItem) item));
		}
	}
	
	private class FTPFileGroupFilesDataProvider {
		public void load() {
			FTPService.Util.getInstance().getUserFTPFileItems(new Integer(1), new AsyncCallback() {

				public void onFailure(Throwable caught) {
				}

				public void onSuccess(Object result) {
					List list = (List) result;
				}
				
			});
			
			/*HTTPRequest.asyncGet(GWT.getModuleBaseURL()+CookieCloaker.DEFAULT_INSTANCE.ftpPathListURL(), new ResponseTextHandler() {
				public void onCompletion(String responseText) {
					fileGrid.clear();
					
					try {
						JSONResponse jsonResponse = JSONResponse.newInstance(responseText);
						jsonResponse.handleErrors();
						
						if (jsonResponse.getResult().get("pathList").isNull() == null) {
							JSONArray arPaths = (JSONArray) jsonResponse.getResult().get("pathList");
							
							for (int i = 0; i < arPaths.size(); i++)
								if (((JSONValue) arPaths.get(i)).isNull() == null)
									fileGrid.addItem(FTPFileItem.getInstance((JSONObject) arPaths.get(i)));
							
							fileGrid.sort();
						}
					}
					catch (Throwable e) {
						GWTErrorLogger.logError(e);
						e.printStackTrace();
					}
				}
			});*/
		}
	}
	
	private class FileSaver {
		private static final int SAVE_DELAY = 2000;
		
		private boolean isSaveInProgress = false;
		private boolean isSaveBeingDelayed = false;
		private boolean isSaveAfterDelay = false;
		private final DirtyPage dirtyPage = new DirtyPage();
		
		private class DirtyPage implements WindowCloseListener {
			public void onWindowClosed() {
			}

			public String onWindowClosing() {
				if ((isSaveBeingDelayed && isSaveAfterDelay) || isSaveInProgress)
					return "Not all data has finished saving. This should only take a few more seconds. Close anyway and lose changes?";
				return null;
			}
		}
		
		public void save() {
			if (isSaveBeingDelayed || isSaveInProgress) {
				isSaveAfterDelay = true;
				Window.addWindowCloseListener(dirtyPage);
				delaySave();
				return;
			}
			isSaveInProgress = true;
			try {
				saveNow();
			}
			catch (RequestException e) {
				Window.alert("Failed to save FTP file data. " + e.toString());
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
					System.out.println("timer run");
					if (isSaveAfterDelay) {
						isSaveAfterDelay = false;
						System.out.println("calling save");
						try {
							saveNow();
						} catch (RequestException e) {
							Window.alert("Failed to save FTP file data. " + e.toString());
						}
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
				ftpSiteId = ftpFileItem.getFtpSite().getFtpSiteId();
				
				if ((sb = (StringBuffer) paramStringBySiteId.get(ftpSiteId)) == null)
					paramStringBySiteId.put(ftpSiteId, sb = new StringBuffer());
				
				if (sb.length() != 0)
					sb.append(LINE_SEPARATOR);
				
				sb.append(ftpFileItem.getType() + ":" + ftpFileItem.getFullPath());
			}
			
			for (it = paramStringBySiteId.keySet().iterator(); it.hasNext();) {
				ftpSiteId = (Integer) it.next();
				sb = (StringBuffer) paramStringBySiteId.get(ftpSiteId);
				//requestBuilder.addParameter(ftpSiteId.toString(), sb.toString());
				requestBuilder.addParameter(ftpSiteId.toString(), Base64Util.encode(sb.toString()));
			}
			
			requestBuilder.setTimeoutMillis(10000);
			requestBuilder.sendRequest(new SaveResponseHandler());
		}
		
		private class SaveResponseHandler implements RequestCallback {
			public void onError(Request request, Throwable exception) {
				GWTErrorLogger.logError(exception);
			}

			public void onResponseReceived(Request request, Response response) {
				if (isSaveInProgress = isSaveAfterDelay)
					Window.removeWindowCloseListener(dirtyPage);
				
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
