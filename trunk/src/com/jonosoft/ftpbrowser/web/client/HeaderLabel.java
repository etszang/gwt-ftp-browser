/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.user.client.ui.Label;

/**
 * @author Jkelling
 *
 */
public class HeaderLabel extends Label {

	public HeaderLabel() {
		super();
		init();
	}

	public HeaderLabel(String text, boolean wordWrap) {
		super(text, wordWrap);
		init();
	}

	public HeaderLabel(String text) {
		super(text);
		init();
	}
	
	private void init() {
		addStyleName("cc-label-header");
	}

}
