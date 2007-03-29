/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * @author Jkelling
 *
 */
public class PopupOverlayPanel extends PopupPanel {
	
	private static int	defaultOverlayOpacity			= 70;
	private static String	defaultOverlayBackgroundColor	= "#e2e2e2";
	
	private final Element	overlayDiv	= DOM.createElement("div");
	private int	overlayOpacity			= defaultOverlayOpacity;
	private String	overlayBackgroundColor	= defaultOverlayBackgroundColor;
	
	public PopupOverlayPanel() {
		super();
		init();
	}
	
	public PopupOverlayPanel(boolean autoHide) {
		super(autoHide);
		init();
	}
	
	public PopupOverlayPanel(int overlayOpacity, String overlayBackgroundColor) {
		this();
		setOverlayOpacity(overlayOpacity);
		setOverlayBackgroundColor(overlayBackgroundColor);
	}
	
	public PopupOverlayPanel(int overlayOpacity, String overlayBackgroundColor, boolean autoHide) {
		this(autoHide);
		setOverlayOpacity(overlayOpacity);
		setOverlayBackgroundColor(overlayBackgroundColor);
	}
	
	private void init() {
		addPopupListener(new OverlayPopupListener());
	}
	
	public void show() {
		super.show();

		DOM.setStyleAttribute(overlayDiv, "position", "absolute");
		DOM.setStyleAttribute(overlayDiv, "zIndex", "99998");
		DOM.setStyleAttribute(overlayDiv, "top", "0px");
		DOM.setStyleAttribute(overlayDiv, "left", "0px");
		DOM.setStyleAttribute(overlayDiv, "opacity", Integer.toString(overlayOpacity/100));
		DOM.setStyleAttribute(overlayDiv, "backgroundColor", overlayBackgroundColor);
		DOM.setStyleAttribute(overlayDiv, "width", Integer.toString(Window.getClientWidth()));
		DOM.setStyleAttribute(overlayDiv, "height", Integer.toString(Window.getClientHeight()));
		DOM.setStyleAttribute(overlayDiv, "filter", "alpha(opacity="+overlayOpacity+")");
		DOM.setStyleAttribute(getElement(), "zIndex", "99999");
		
		DOM.insertChild(DOM.getParent(getElement()), overlayDiv, DOM.getChildIndex(DOM.getParent(getElement()), getElement()));
	}
	
	private class OverlayPopupListener implements PopupListener {
		public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
			DOM.removeChild(DOM.getParent(overlayDiv), overlayDiv);
		}
	}
	
	public static int getDefaultOverlayOpacity() {
		return defaultOverlayOpacity;
	}
	
	public static void setDefaultOverlayOpacity(int defaultOverlayOpacity) {
		PopupOverlayPanel.defaultOverlayOpacity = defaultOverlayOpacity;
	}
	
	public static String getDefaultOverlayBackgroundColor() {
		return defaultOverlayBackgroundColor;
	}
	
	public static void setDefaultOverlayBackgroundColor(String defaultOverlayBackgroundColor) {
		PopupOverlayPanel.defaultOverlayBackgroundColor = defaultOverlayBackgroundColor;
	}
	
	public int getOverlayOpacity() {
		return overlayOpacity;
	}
	
	public void setOverlayOpacity(int overlayOpacity) {
		this.overlayOpacity = overlayOpacity;
	}
	
	public String getOverlayBackgroundColor() {
		return overlayBackgroundColor;
	}
	
	public void setOverlayBackgroundColor(String overlayBackgroundColor) {
		this.overlayBackgroundColor = overlayBackgroundColor;
	}
	
}
