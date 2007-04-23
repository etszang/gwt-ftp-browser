package com.jonosoft.ftpbrowser.web.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.google.gwt.core.client.GWT;
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
public class FTPFileGroupWidget extends Composite implements LoadsRemoteData {
	private final FTPFileItemSelectGrid fileGrid = new FTPFileItemSelectGrid();
	private final FileSaver fileSaver = new FileSaver();
	private FTPFileItemSelectGrid sourceFileGrid = null;
	private final FDAS fdsa = new FDAS();
	private FTPFileGroup ftpFileGroup = null;
	
	public FTPFileGroupWidget() {
		initWidget(fileGrid);

		fileGrid.setDisplayFullPaths(true);

		addStyleName("cc-filegroupwidget");
	}
	
	public void reloadRemoteData() {
		reloadRemoteData(null);
	}
	
	public void reloadRemoteData(final AsyncCallback callback) {
		DeferredCommand.add(new Command() {
			public void execute() {
				FTPService.Util.getInstance().getUserFTPFileItems(new Integer(1), new AsyncCallback() {
					public void onFailure(Throwable caught) {
						GWTErrorLogger.logError(caught);
						caught.printStackTrace();
						
						if (callback != null)
							callback.onFailure(caught);
					}
					public void onSuccess(Object result) {
						fileGrid.clear();

						List list = (List) result;

						for (Iterator it = list.iterator(); it.hasNext();)
							fileGrid.addItem((FTPFileItem) it.next());

						fileGrid.sort();
						
						if (callback != null)
							callback.onSuccess(result);
					}
				});
			}
		});
	}

	public FTPFileItemSelectGrid getSourceFileGrid() {
		return sourceFileGrid;
	}
	
	public FTPFileGroup getFTPFileGroup() {
		return ftpFileGroup;
	}
	
	public void setFTPFileGroup(FTPFileGroup ftpFileGroup) {
		this.ftpFileGroup = ftpFileGroup;
	}
	
	public List getFTPFileGroupItems() {
		final List items = new Vector();
		FTPFileItem fileItem;
		FTPFileGroupItem groupItem;
		
		for (Iterator it = fileGrid.getItems().iterator(); it.hasNext();) {
			fileItem = (FTPFileItem) it.next();
			groupItem = new FTPFileGroupItem();
			
			if (getFTPFileGroup() != null)
				groupItem.setFtpFileGroupId(getFTPFileGroup().getFtpFileGroupId());
			groupItem.setFtpSiteId(fileItem.getFtpSiteId());
			groupItem.setName(fileItem.getName());
			groupItem.setType(fileItem.getType());
			groupItem.setFullPath(fileItem.getFullPath());
			
			items.add(groupItem);
		}
		
		return items;
	}

	public void setSourceFileGrid(final FTPFileItemSelectGrid sourceFileGrid) {
		if (this.sourceFileGrid != null && this.sourceFileGrid != sourceFileGrid)
			this.sourceFileGrid.removeItemSelectGridListener(fdsa);
		this.sourceFileGrid = sourceFileGrid;
		this.sourceFileGrid.addItemSelectGridListener(fdsa);
	}

	private boolean containsFTPFileItem(FTPFileItem itemToFind) {
		return fileGrid.getItems().contains(itemToFind);
	}

	// TODO Rename this ItemSelectGridListener class from FDAS
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
			catch (Exception e) {
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
						} catch (Exception e) {
							Window.alert("Failed to save FTP file data. " + e.toString());
						}
					}
				}
			}.schedule(SAVE_DELAY);
		}

		private void saveNow() {
			FTPService.Util.getInstance().saveUserFTPFileItems(new Integer(1), new ArrayList(fileGrid.getItems()), new SaveResponseHandler());
		}

		private class SaveResponseHandler implements AsyncCallback {
			public void onFailure(Throwable caught) {
				GWTErrorLogger.logError(caught);
				GWT.log("Saving FTPFileGroup contents failed", caught);
			}

			public void onSuccess(Object result) {
				if (isSaveInProgress = isSaveAfterDelay)
					Window.removeWindowCloseListener(dirtyPage);
			}
		}
	 }
}
