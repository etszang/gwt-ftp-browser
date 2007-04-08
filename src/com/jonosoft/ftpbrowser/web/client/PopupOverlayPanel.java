/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import org.gwtwidgets.client.wrap.Callback;
import org.gwtwidgets.client.wrap.Effect;
import org.gwtwidgets.client.wrap.EffectOption;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jkelling
 *
 */
public class PopupOverlayPanel extends PopupPanel {
	
	private final Command effectCommand	= new EffectCommand(this);
	private final OverlayPanel overlayPanel = new OverlayPanel();
	
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
		overlayPanel.show();
		super.show();
		
		DOM.setStyleAttribute(getElement(), "zIndex", "99999");
		
		DOM.setStyleAttribute(getElement(), "visibility", "hidden");
		DOM.setStyleAttribute(getElement(), "display", "block");
		
		DeferredCommand.add(new Command() {
			public void execute() {
				setPopupPosition((Window.getClientWidth() / 2)- (getOffsetWidth() / 2), (Window.getClientHeight() / 2) - (getOffsetHeight() / 2));
				DOM.setStyleAttribute(getElement(), "visibility", "visible");
				DOM.setStyleAttribute(getElement(), "display", "none");
				DeferredCommand.add(effectCommand);
			}
		});
	}
	
	public void hide() {
		overlayPanel.hide();
		Effect.fade(this, new EffectOption[] {new EffectOption("duration", "0.2"), new EffectOption("afterFinish", new Callback() {
			public void execute() {
				finishHide();
			}
		})});
	}
	
	private void finishHide() {
		super.hide();
	}
	
	private class OverlayPopupListener implements PopupListener {
		public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
			overlayPanel.hide();
		}
	}
	
	public int getOverlayOpacity() {
		return overlayPanel.getOverlayOpacity();
	}
	
	public void setOverlayOpacity(int overlayOpacity) {
		overlayPanel.setOverlayOpacity(overlayOpacity);
	}
	
	public String getOverlayBackgroundColor() {
		return overlayPanel.getOverlayBackgroundColor();
	}
	
	public void setOverlayBackgroundColor(String overlayBackgroundColor) {
		overlayPanel.setOverlayBackgroundColor(overlayBackgroundColor);
	}
	
	private class EffectCommand implements Command {
		private Widget widget = null;
		
		public EffectCommand(Widget widget) {
			this.widget = widget;
		}
		
		public void execute() {
			Effect.appear(widget, new EffectOption[] {new EffectOption("duration", "0.2")});
		}
	}
	
}
