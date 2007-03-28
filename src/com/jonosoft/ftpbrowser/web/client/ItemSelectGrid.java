/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.UIObject;

/**
 * <p>
 * An implementation of {@link Grid} where individual rows are selectable by
 * clicking on them.
 * </p>
 * 
 * <p>
 * Each row in the grid has an item (Object) associated with it that is passed
 * along to event listeners when items are selected or deselected.
 * </p>
 * 
 * @author Jkelling
 */
public class ItemSelectGrid extends Grid {
	
	private static final String STYLE_NAME_PREFIX = "gwt-itemselectgrid";
	
	final private Vector listeners = new Vector();
	final private HashMap itemsByRow = new HashMap();
	final private HashMap rowsByItem = new HashMap();
	final private HashSet selectedItems = new HashSet();
	final private HashSet previousSelectedItems = new HashSet();
	
	private int dragMode = DragMode.OFF;
	private int mouseDownX;
	private int mouseDownY;
	private boolean mouseDown = false;
	private Element mouseDownRow = null;
	private int mouseDownRowIndex;
	private int mouseMoveRowIndex;
	private boolean allowMultipleSelect = true; // TODO: Implement allowMultipleSelect field
	
	
	private static interface DragMode {
		int OFF = 0;
		int SELECT = 1;
		int DESELECT = 2;
	}

	public ItemSelectGrid() {
		super();
		init();
	}

	public ItemSelectGrid(int rows, int columns) {
		super(rows, columns);
		init();
	}
	
	private void init() {
		setCellSpacing(0);
		setCellPadding(2);
		
		addStyleName(STYLE_NAME_PREFIX);
	}
	
	public void addItem(Object item) {
		if (itemsByRow.containsValue(item))
			return;
		resizeRows(getRowCount()+1);
		setItem(getRowCount()-1, item);
	}
	
	public void setItem(int row, Object item) {
		itemsByRow.put(getRowFormatter().getElement(row), item);
		rowsByItem.put(item, getRowFormatter().getElement(row));
		buildItemRow(row, item);
	}
	
	public Object getItem(int row) {
		return itemsByRow.get(getRowFormatter().getElement(row));
	}
	
	public boolean getAllowMultipleSelections() {
		return allowMultipleSelect;
	}
	
	public void setAllowMultipleSelections(boolean allowMultipleSelect) {
		this.allowMultipleSelect = allowMultipleSelect;
	}
	
	/**
	 * Override this method to specify the display logic for building a row in
	 * the Grid. This method is called automatically when an item is set using
	 * {@link #setItem(int, Object)} or by {@link #refreshDisplay()}.
	 * 
	 * @param row
	 * @param item
	 *            Object set by {@link #setItem(int, Object)} -- null if no
	 *            Object was provided.
	 */
	public void buildItemRow(int row, Object item) {
	}
	
	/**
	 * Refreshes the items in the Grid by calling
	 * {@link #buildItemRow(int, Object)} for each row.
	 */
	public void refreshDisplay() {
		for (int i = 0; i < getRowCount(); i++)
			buildItemRow(i, getItem(i));
	}
	
	/**
	 * @see com.google.gwt.user.client.ui.Grid#resizeColumns(int)
	 */
	public void resizeColumns(int columns) {
		super.resizeColumns(columns);
	}

	/**
	 * @see com.google.gwt.user.client.ui.Grid#resizeRows(int)
	 */
	public void resizeRows(int rows) {
		int oldRowCount = getRowCount();
		int newRowCount = rows - oldRowCount;
		super.resizeRows(rows);
		if (newRowCount > 0)
			setupRows(oldRowCount);
	}
	
	/**
	 * @see com.google.gwt.user.client.ui.HTMLTable#clear()
	 */
	public void clear() {
		super.clear();
		itemsByRow.clear();
		selectedItems.clear();
		previousSelectedItems.clear();
	}
	
	public void removeItemAndRow(Object item) {
		if (rowsByItem.containsKey(item)) {
			Element rowElement = (Element) rowsByItem.get(item);
			itemsByRow.remove(rowElement);
			rowsByItem.remove(item);
			DOM.removeChild(DOM.getParent(rowElement), rowElement);
			--numRows;
		}
	}

	private void makeCellTextUnselectableForRow(int rowIndex) {
		for (int i = 0; i < getColumnCount(); i++) {
			DOM.setAttribute(getCellFormatter().getElement(rowIndex, i), "unselectable", "on");
			DOM.setStyleAttribute(getCellFormatter().getElement(rowIndex, i), "MozUserSelect", "none");
		}
	}
	
	private void setupRows(int startRowIndex) {
		if (startRowIndex >= 0) {
			for (int r = startRowIndex; r < getRowCount(); r++) {
				final int rowIndex = r;
				final Element rowElement = getRowFormatter().getElement(r);

				makeCellTextUnselectableForRow(rowIndex);

				DOM.sinkEvents(rowElement, (Event.MOUSEEVENTS | DOM.getEventsSunk(rowElement)) ^ Event.ONMOUSEUP);
				DOM.setEventListener(rowElement, new EventListener() {
					public void onBrowserEvent(Event event) {
						switch (DOM.eventGetType(event)) {
						case Event.ONMOUSEOVER:
							if (! getRowSelectState(rowElement))
								getRowFormatter().addStyleName(rowIndex, STYLE_NAME_PREFIX + "-item-hover");
							break;

						case Event.ONMOUSEOUT:
							getRowFormatter().removeStyleName(rowIndex, STYLE_NAME_PREFIX + "-item-hover");
							break;

						case Event.ONMOUSEMOVE:
							//if (DOM.eventGetButton(event) == Event.BUTTON_LEFT) {
							if (mouseDown) {
								switch (dragMode) {
								case DragMode.OFF:
									int currentMouseX = DOM.eventGetClientX(event);
									int currentMouseY = DOM.eventGetClientY(event);
									if (Math.abs(mouseDownX - currentMouseX) < 3 && Math.abs(mouseDownY - currentMouseY) > 4) {
										// We don't need to change the state of the mouseDownRow
										// since that was done on the ONMOUSEDOWN event, but we will
										// use it's current state to determine the DragMode.
										if (getRowSelectState(mouseDownRow) || ! getAllowMultipleSelections())
											dragMode = DragMode.SELECT;
										else
											dragMode = DragMode.DESELECT;
										// We do need to change the state of the current rowElement
										// if it's different than the mouseDownRow
										if (! rowElement.equals(mouseDownRow))
											//setRowSelectState(rowElement, dragMode == DragMode.SELECT);
											setRowSelectStateFromPreviousSelectState(rowElement, false);
									}
									break;

								case DragMode.SELECT:
								case DragMode.DESELECT:
								default:
									if (getAllowMultipleSelections()) {
										if (mouseMoveRowIndex > rowIndex && mouseMoveRowIndex > mouseDownRowIndex)
											setMultiRowSelectStateFromPreviousSelectState(Math.max(rowIndex, mouseDownRowIndex)+1, mouseMoveRowIndex, true);
										else if (mouseMoveRowIndex < rowIndex && mouseMoveRowIndex < mouseDownRowIndex)
											setMultiRowSelectStateFromPreviousSelectState(Math.min(rowIndex, mouseDownRowIndex)-1, mouseMoveRowIndex, true);
										mouseMoveRowIndex = rowIndex;
										setMultiRowSelectStateFromPreviousSelectState(mouseDownRowIndex, rowIndex, false);
									}
									else
										setRowSelectState(rowElement, dragMode == DragMode.SELECT);
									break;
								}
							}
							break;

						case Event.ONMOUSEDOWN:
							if (! mouseDown) {
								mouseDown = true;
								mouseDownX = DOM.eventGetClientX(event);
								mouseDownY = DOM.eventGetClientY(event);
								mouseDownRow = rowElement;
								mouseDownRowIndex = rowIndex;
								mouseMoveRowIndex = rowIndex;
								dragMode = DragMode.OFF;
								previousSelectedItems.clear();
								previousSelectedItems.addAll(selectedItems);
								setRowSelectState(rowElement, ! getRowSelectState(rowElement));
								// TODO: Save existing sunk events and event listener if they exist and restore on mouseup
								DOM.sinkEvents(RootPanel.getBodyElement(), Event.ONMOUSEUP);
								DOM.setEventListener(RootPanel.getBodyElement(), this);
							}
							else {
								// This may occur if the mouse button was
								// released after the cursor was moved outside
								// of the window bounds
							}
							break;

						case Event.ONMOUSEUP:
							mouseDown = false;
							DOM.sinkEvents(RootPanel.getBodyElement(), 0);
							DOM.setEventListener(RootPanel.getBodyElement(), null);
							fireSelectStateChange();
							break;

						default:
							break;
						}
					}
				});
			}
		}
	}
	
	public void setRowSelectStateFromPreviousSelectState(Element row, boolean matchPreviousSelectState) {
		setRowSelectState(row, getRowPreviousSelectState(row) == matchPreviousSelectState);
	}
	
	/**
	 * @param startIndex Inclusive
	 * @param endIndex Inclusive
	 */
	public void setMultiRowSelectStateFromPreviousSelectState(int startIndex, int endIndex, boolean matchPreviousSelectState) {
		int min = Math.min(startIndex, endIndex);
		int max = Math.max(startIndex, endIndex);
		for (int i = min; i <= max; i++)
			setRowSelectStateFromPreviousSelectState(getRowFormatter().getElement(i), matchPreviousSelectState);
	}
	
	public void setMultiRowSelectStateFromPreviousSelectState(Collection rows, boolean matchPreviousSelectState) {
		Collection safeRows = getSafeSelectedRowsList(rows);
		for (Iterator it = safeRows.iterator(); it.hasNext();)
			setRowSelectStateFromPreviousSelectState((Element) it.next(), matchPreviousSelectState);
	}
	
	public boolean getRowPreviousSelectState(Element row) {
		return previousSelectedItems.contains(row);
	}
	
	public boolean getRowSelectState(Element row) {
		return selectedItems.contains(row);
	}
	
	public void setRowSelectState(Element row, boolean state) {
		if (state) {
			onBeforeSelectRow(row);
			selectedItems.add(row);
		}
		else
			selectedItems.remove(row);
		UIObject.setStyleName(row, STYLE_NAME_PREFIX + "-item-selected", state);
	}
	
	/**
	 * @param startIndex Inclusive
	 * @param endIndex Inclusive
	 */
	protected void setMultiRowSelectState(int startIndex, int endIndex, boolean state) {
		int min = Math.min(startIndex, endIndex);
		int max = Math.max(startIndex, endIndex);
		for (int i = min; i <= max; i++)
			setRowSelectState(getRowFormatter().getElement(i), state);
	}
	
	protected void setMultiRowSelectState(Collection rows, boolean state) {
		Collection safeRows = getSafeSelectedRowsList(rows);
		for (Iterator it = safeRows.iterator(); it.hasNext();)
			setRowSelectState((Element) it.next(), state);
	}
	
	private Collection getSafeSelectedRowsList(Collection rows) {
		if (! rows.equals(selectedItems))
			return rows;
		else {
			Collection safeRows = new Vector();
			safeRows.addAll(rows);
			return safeRows;
		}
	}
	
	private void onBeforeSelectRow(Element row) {
		if (! getAllowMultipleSelections()) {
			Collection otherSelectedItems = new Vector();
			otherSelectedItems.addAll(selectedItems);
			otherSelectedItems.remove(row);
			setMultiRowSelectState(otherSelectedItems, false);
		}
	}
	
	public void addItemSelectGridListener(ItemSelectGridListener listener) {
		listeners.add(listener);
	}
	
	public void removeItemSelectGridListener(ItemSelectGridListener listener) {
		listeners.remove(listener);
	}
	
	private void fireSelectStateChange() {
		if (listeners.size() > 0) {
			Collection selectedItemsClone = (Collection) selectedItems.clone();
			Collection previousSelectedItemsClone = (Collection) previousSelectedItems.clone();
			
			selectedItemsClone.removeAll(previousSelectedItems);
			previousSelectedItemsClone.removeAll(selectedItems);
			
			Collection added = new Vector();
			Collection removed = new Vector();

			for (Iterator it = selectedItemsClone.iterator(); it.hasNext();)
				added.add(itemsByRow.get(it.next()));
			for (Iterator it = previousSelectedItemsClone.iterator(); it.hasNext();)
				removed.add(itemsByRow.get(it.next()));
			
			if (previousSelectedItemsClone.size() > 0)
				fireSelectStateChange(this, removed, false);
			if (selectedItemsClone.size() > 0)
				fireSelectStateChange(this, added, true);
		}
	}
	
	/**
	 * Fires a select state change event to all listeners.
	 * 
	 * @param sender
	 *            the widget sending the event.
	 */
	private void fireSelectStateChange(ItemSelectGrid sender, Collection items, boolean state) {
		for (Iterator it = listeners.iterator(); it.hasNext();)
			((ItemSelectGridListener) it.next()).onItemsSelectStateChanged(this, items, state);
	}

	
}
