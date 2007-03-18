/**
 * 
 */
package com.cookiecloaker.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author Jkelling
 *
 */
public class Web implements EntryPoint {

	/* (non-Javadoc)
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	public void onModuleLoad() {
		RootPanel.get().add(new FTPTree());
	}

}
