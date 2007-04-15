package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class TestEntryPoint implements EntryPoint {

	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get();
		final HorizontalPanel horizontalPanel = new HorizontalPanel();
		rootPanel.add(horizontalPanel);
		final TextBox x = new TextBox();
		horizontalPanel.add(x);
		final TextBox y = new TextBox();
		horizontalPanel.add(y);
		final Label z = new Label("New Label");
		horizontalPanel.add(z);
		final Button addButton = new Button();
		rootPanel.add(addButton);
		addButton.addClickListener(new AddButtonClickListener());
		addButton.setText("Add");
	}
	private class AddButtonClickListener implements ClickListener {
		public void onClick(Widget sender) {
			
		}
	}

}
