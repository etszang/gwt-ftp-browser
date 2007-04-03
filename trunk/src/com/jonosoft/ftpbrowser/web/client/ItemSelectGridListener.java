package com.jonosoft.ftpbrowser.web.client;

import java.util.List;

/**
 * @author Jkelling
 *
 */
public interface ItemSelectGridListener {
	
	void onItemsSelectStateChanged(ItemSelectGrid sender, List items, boolean state);
	
	void onItemAdded(ItemSelectGrid sender, Object item);
	
}
