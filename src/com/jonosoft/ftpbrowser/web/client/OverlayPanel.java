/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import org.gwtwidgets.client.wrap.Callback;
import org.gwtwidgets.client.wrap.Effect;
import org.gwtwidgets.client.wrap.EffectOption;
import org.gwtwidgets.client.wwrapper.WPanel;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jkelling
 *
 */
public class OverlayPanel extends WPanel {
	
	private static int	defaultOverlayOpacity			= 70;
	private static String	defaultOverlayBackgroundColor	= "#e2e2e2";
	
	private int	overlayOpacity			= defaultOverlayOpacity;
	private String	overlayBackgroundColor	= defaultOverlayBackgroundColor;
	private final Command effectCommand	= new EffectCommand(this);
	
	public OverlayPanel() {
		super("overlay");
	}
	
	public OverlayPanel(int overlayOpacity, String overlayBackgroundColor) {
		this();
		setOverlayOpacity(overlayOpacity);
		setOverlayBackgroundColor(overlayBackgroundColor);
	}
	
	public static int getDefaultOverlayOpacity() {
		return defaultOverlayOpacity;
	}
	
	public static void setDefaultOverlayOpacity(int defaultOverlayOpacity) {
		OverlayPanel.defaultOverlayOpacity = defaultOverlayOpacity;
	}
	
	public static String getDefaultOverlayBackgroundColor() {
		return defaultOverlayBackgroundColor;
	}
	
	public static void setDefaultOverlayBackgroundColor(String defaultOverlayBackgroundColor) {
		OverlayPanel.defaultOverlayBackgroundColor = defaultOverlayBackgroundColor;
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
	
	public void show() {
		/*DOM.removeChild(getParent().getElement(), getElement());
		DOM.appendChild(RootPanel.get("gwt-content").getElement(), getElement());*/
		
		/*DOM.setStyleAttribute(getElement(), "position", "absolute");
		DOM.setStyleAttribute(getElement(), "zIndex", "99998");
		DOM.setStyleAttribute(getElement(), "top", "0px");
		DOM.setStyleAttribute(getElement(), "left", "0px");
		DOM.setStyleAttribute(getElement(), "opacity", "0.70");
		DOM.setStyleAttribute(getElement(), "backgroundColor", overlayBackgroundColor);
		DOM.setStyleAttribute(getElement(), "width", Integer.toString(Window.getClientWidth()));
		DOM.setStyleAttribute(getElement(), "height", Integer.toString(Window.getClientHeight()));
		DOM.setStyleAttribute(getElement(), "filter", "alpha(opacity="+overlayOpacity+")");*/
		

		setPixelSize(Window.getClientWidth(), Window.getClientHeight());
		DOM.setStyleAttribute(getElement(), "backgroundColor", overlayBackgroundColor);
		DOM.setStyleAttribute(getElement(), "display", "none");
		DOM.setStyleAttribute(getElement(), "zIndex", "99998");
		DOM.setStyleAttribute(getElement(), "left", "0px");
		DOM.setStyleAttribute(getElement(), "top", "0px");
		
		DeferredCommand.add(new Command() {
			public void execute() {
				/*DOM.setStyleAttribute(getElement(), "visibility", "visible");
				DOM.setStyleAttribute(getElement(), "display", "none");*/
				DeferredCommand.add(effectCommand);
			}
		});
	}
	
	public void hide() {
		Effect.fade(this, new EffectOption[] {new EffectOption("duration", "0.2"), new EffectOption("afterFinish", new Callback() {
			public void execute() {
				finishHide();
			}
		})});
	}
	
	private void finishHide() {
		//super.hide();
	}
	
	private class EffectCommand implements Command {
		private Widget widget = null;
		
		public EffectCommand(Widget widget) {
			this.widget = widget;
		}
		
		public void execute() {
			Effect.appear(widget, new EffectOption[] {new EffectOption("duration", "0.2"), new EffectOption("from", "0.1"), new EffectOption("to", "0.7")});
		}
	}
}
