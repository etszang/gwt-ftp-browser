package com.jonosoft.ftpbrowser.web.client;

import java.util.Collection;

/**
 * @author Jkelling
 *
 */
public interface ItemSelectGridListener {
	
	void onItemsSelectStateChanged(ItemSelectGrid sender, Collection items, boolean state);
	
}
