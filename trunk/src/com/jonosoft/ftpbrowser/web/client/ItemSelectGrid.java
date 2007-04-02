/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ListBox;
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
	final private HashSet selectedRows = new HashSet();
	final private HashSet previousSelectedRows = new HashSet();
	
	private int dragMode = DragMode.OFF;
	private int mouseDownX;
	private int mouseDownY;
	private boolean mouseDown = false;
	private Element mouseDownRow = null;
	private int mouseDownRowIndex;
	private int mouseMoveRowIndex;
	private boolean multipleSelect = true; // TODO: Implement allowMultipleSelect field
	
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
	
	public void addItemSelectGridListener(ItemSelectGridListener listener) {
		listeners.add(listener);
	}

	public void addItem(Object item) {
		if (itemsByRow.containsValue(item))
			return;
		resizeRows(getRowCount()+1);
		setItem(getRowCount()-1, item);
	}
	
	/**
	 * <p>
	 * Treating grid rows more like {@link ListBox} items, this method removes
	 * all row elements from the grid.
	 * </p>
	 * 
	 * <p>
	 * This method works as if calling {@link #resizeRows(int) resizeRows(0)}.
	 * Passing 0 to resizeRows will still throw an exception, however.
	 * </p>
	 * 
	 * @see com.google.gwt.user.client.ui.HTMLTable#clear()
	 */
	public void clear() {
		while (numRows > 0)
			removeRow(--numRows);
		itemsByRow.clear();
		rowsByItem.clear();
		selectedRows.clear();
		previousSelectedRows.clear();
	}
	
	public void setItem(int row, Object item) {
		itemsByRow.put(getRowFormatter().getElement(row), item);
		rowsByItem.put(item, getRowFormatter().getElement(row));
		buildItemRow(row, item);
	}
	
	public Object getItem(int row) {
		return itemsByRow.get(getRowFormatter().getElement(row));
	}
	
	public Set getItems() {
		return rowsByItem.keySet();
	}
	
	public boolean isItemSelected(Object item) {
		return selectedRows.contains(rowsByItem.get(item));
	}
	
	public boolean isRowSelected(Element row) {
		return selectedRows.contains(row);
	}

	public boolean isMultipleSelect() {
		return multipleSelect;
	}
	
	/**
	 * <p>
	 * Gets the selected state of the specified row before the change to the
	 * selected items of the grid. This includes when the mouse was clicked, but
	 * the selection did not change--this caveat may change in the future.
	 * </p>
	 * 
	 * <p>
	 * This is final just so that it is not private and so no one overrides it.
	 * Not that it would be, but the way items are sent to the listeners depends
	 * on this.
	 * </p>
	 * 
	 * @param row
	 * @return
	 */
	public final boolean getRowSelectedPreviously(Element row) {
		return previousSelectedRows.contains(row);
	}

	public void removeItemSelectGridListener(ItemSelectGridListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Removes both the item object and the row element for the specified item
	 * object.
	 * 
	 * @param item
	 *            item object to remove--also removes assocaited row element
	 */
	public void removeItem(Object item) {
		if (rowsByItem.containsKey(item)) {
			Element rowElement = (Element) rowsByItem.remove(item);
			itemsByRow.remove(rowElement);
			DOM.removeChild(DOM.getParent(rowElement), rowElement);
			--numRows;
		}
	}

	/**
	 * Removes both the row element and the item object at the specified index.
	 * the row count is decremented by one.
	 * 
	 * @param row
	 *            index of row and associated item to remove
	 */
	public void removeItem(int row) {
		removeItem(itemsByRow.get(getRowFormatter().getElement(row)));
	}

	public void setItemSelected(Element row, boolean state) {
		if (state) {
			onBeforeSelectRow(row);
			selectedRows.add(row);
		}
		else
			selectedRows.remove(row);
		UIObject.setStyleName(row, STYLE_NAME_PREFIX + "-item-selected", state);
	}

	public void setMultipleSelect(boolean multipleSelect) {
		this.multipleSelect = multipleSelect;
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
	 * Same as the {@link Grid}'s resizeRows but additionally adds event
	 * handling for selecing rows
	 * 
	 * @see com.google.gwt.user.client.ui.Grid#resizeRows(int)
	 */
	public void resizeRows(int rows) {
		int oldRowCount = getRowCount();
		int newRowCount = rows - oldRowCount;
		super.resizeRows(rows);
		if (newRowCount > 0)
			setupRows(oldRowCount);
	}
	
	private void setRowSelectedFromPreviousSelectState(Element row, boolean matchPreviousSelectState) {
		setItemSelected(row, getRowSelectedPreviously(row) == matchPreviousSelectState);
	}
	
	/**
	 * @param startIndex Inclusive
	 * @param endIndex Inclusive
	 */
	private void setMultipleRowsSelectedFromPreviousSelectState(int startIndex, int endIndex, boolean matchPreviousSelectState) {
		int min = Math.min(startIndex, endIndex);
		int max = Math.max(startIndex, endIndex);
		for (int i = min; i <= max; i++)
			setRowSelectedFromPreviousSelectState(getRowFormatter().getElement(i), matchPreviousSelectState);
	}
	
	private void setMultipleRowsSelectedFromPreviousSelectState(List rows, boolean matchPreviousSelectState) {
		List safeRows = getSafeSelectedRowsList(rows);
		for (Iterator it = safeRows.iterator(); it.hasNext();)
			setRowSelectedFromPreviousSelectState((Element) it.next(), matchPreviousSelectState);
	}
	
	/**
	 * @param startIndex Inclusive
	 * @param endIndex Inclusive
	 */
	private void setMultipleItemsSelected(int startIndex, int endIndex, boolean state) {
		int min = Math.min(startIndex, endIndex);
		int max = Math.max(startIndex, endIndex);
		for (int i = min; i <= max; i++)
			setItemSelected(getRowFormatter().getElement(i), state);
	}
	
	private void setMultipleItemsSelected(List rows, boolean state) {
		List safeRows = getSafeSelectedRowsList(rows);
		for (Iterator it = safeRows.iterator(); it.hasNext();)
			setItemSelected((Element) it.next(), state);
	}
	
	private List getSafeSelectedRowsList(List rows) {
		if (! rows.equals(selectedRows))
			return rows;
		else {
			List safeRows = new Vector();
			safeRows.addAll(rows);
			return safeRows;
		}
	}
	
	private void onBeforeSelectRow(Element row) {
		if (! isMultipleSelect()) {
			List otherSelectedItems = new Vector();
			otherSelectedItems.addAll(selectedRows);
			otherSelectedItems.remove(row);
			setMultipleItemsSelected(otherSelectedItems, false);
		}
	}
	
	private void init() {
		setCellSpacing(0);
		setCellPadding(2);
		
		addStyleName(STYLE_NAME_PREFIX);
	}

	private void fireSelectStateChange() {
		if (listeners.size() > 0) {
			Set selectedItemsClone = (Set) selectedRows.clone();
			Set previousSelectedItemsClone = (Set) previousSelectedRows.clone();
			
			selectedItemsClone.removeAll(previousSelectedRows);
			previousSelectedItemsClone.removeAll(selectedRows);
			
			List added = new Vector();
			List removed = new Vector();

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
	private void fireSelectStateChange(ItemSelectGrid sender, List items, boolean state) {
		for (Iterator it = listeners.iterator(); it.hasNext();)
			((ItemSelectGridListener) it.next()).onItemsSelectStateChanged(this, items, state);
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
							if (! isRowSelected(rowElement))
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
										if (isRowSelected(mouseDownRow) || ! isMultipleSelect())
											dragMode = DragMode.SELECT;
										else
											dragMode = DragMode.DESELECT;
										// We do need to change the state of the current rowElement
										// if it's different than the mouseDownRow
										if (! rowElement.equals(mouseDownRow))
											//setRowSelectState(rowElement, dragMode == DragMode.SELECT);
											setRowSelectedFromPreviousSelectState(rowElement, false);
									}
									break;
	
								case DragMode.SELECT:
								case DragMode.DESELECT:
								default:
									if (isMultipleSelect()) {
										if (mouseMoveRowIndex > rowIndex && mouseMoveRowIndex > mouseDownRowIndex)
											setMultipleRowsSelectedFromPreviousSelectState(Math.max(rowIndex, mouseDownRowIndex)+1, mouseMoveRowIndex, true);
										else if (mouseMoveRowIndex < rowIndex && mouseMoveRowIndex < mouseDownRowIndex)
											setMultipleRowsSelectedFromPreviousSelectState(Math.min(rowIndex, mouseDownRowIndex)-1, mouseMoveRowIndex, true);
										mouseMoveRowIndex = rowIndex;
										setMultipleRowsSelectedFromPreviousSelectState(mouseDownRowIndex, rowIndex, false);
									}
									else
										setItemSelected(rowElement, dragMode == DragMode.SELECT);
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
								previousSelectedRows.clear();
								previousSelectedRows.addAll(selectedRows);
								setItemSelected(rowElement, ! isRowSelected(rowElement));
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

	
}
