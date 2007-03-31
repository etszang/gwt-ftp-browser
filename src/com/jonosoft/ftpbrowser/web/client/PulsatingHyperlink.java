/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.Date;

import org.gwtwidgets.client.style.Color;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Hyperlink;

/**
 * @author Jkelling
 *
 */
public class PulsatingHyperlink extends Hyperlink {
	// This should not need to change for any reason. The
	// Timer will always run at the same frequency
	private static final int TIMER_PERIOD_MILISECONDS = 30;
	
	private Color baseBackgroundColor = null;
	private Color peakBackgroundColor = null;
	private int durationInMiliseconds = 1400;
	private float[] current = new float[3];
	private boolean goingUp = true;
	private final Timer timer = new PulsateTimer();

	/**
	 * 
	 */
	public PulsatingHyperlink() {
		super();
		init();
	}

	/**
	 * @param text
	 * @param asHTML
	 * @param targetHistoryToken
	 */
	public PulsatingHyperlink(String text, boolean asHTML, String targetHistoryToken) {
		super(text, asHTML, targetHistoryToken);
		init();
	}

	/**
	 * @param text
	 * @param targetHistoryToken
	 */
	public PulsatingHyperlink(String text, String targetHistoryToken) {
		super(text, targetHistoryToken);
		init();
	}

	/**
	 * @param baseBackgroundColor Color
	 * @param peakBackgroundColor Color
	 * @param durationInMiliseconds float The number of seconds to finish one full oscillation -- base to peak to base
	 */
	public PulsatingHyperlink(Color baseBackgroundColor, Color peakBackgroundColor, int durationInMiliseconds) {
		super();
		this.baseBackgroundColor = baseBackgroundColor;
		this.peakBackgroundColor = peakBackgroundColor;
		this.durationInMiliseconds = durationInMiliseconds;
		init();
	}
	
	public PulsatingHyperlink(String text, boolean asHTML, String targetHistoryToken, Color baseBackgroundColor, Color peakBackgroundColor, int durationInMiliseconds) {
		super(text, asHTML, targetHistoryToken);
		this.baseBackgroundColor = baseBackgroundColor;
		this.peakBackgroundColor = peakBackgroundColor;
		this.durationInMiliseconds = durationInMiliseconds;
		init();
	}
	
	public PulsatingHyperlink(String text, String targetHistoryToken, Color baseBackgroundColor, Color peakBackgroundColor, int durationInMiliseconds) {
		super(text, targetHistoryToken);
		this.baseBackgroundColor = baseBackgroundColor;
		this.peakBackgroundColor = peakBackgroundColor;
		this.durationInMiliseconds = durationInMiliseconds;
		init();
	}
	
	protected Element getAnchorElement() {
		return DOM.getFirstChild(getElement());
	}
	
	public void setBorder(int pixelSize, String color, String style) {
		DOM.setStyleAttribute(getAnchorElement(), "borderWidth", pixelSize + "px");
		DOM.setStyleAttribute(getAnchorElement(), "borderColor", color);
		DOM.setStyleAttribute(getAnchorElement(), "borderStyle", style);
		DOM.setStyleAttribute(getElement(), "padding", (pixelSize) + "px 0");
	}
	
	public Color getBaseBackgroundColor() {
		return baseBackgroundColor;
	}
	
	public void setBaseBackgroundColor(Color baseBackgroundColor) {
		this.baseBackgroundColor = baseBackgroundColor;
	}
	
	public Color getPeakBackgroundColor() {
		return peakBackgroundColor;
	}
	
	public void setPeakBackgroundColor(Color peakBackgroundColor) {
		this.peakBackgroundColor = peakBackgroundColor;
	}
	
	public float getDurationInMiliseconds() {
		return this.durationInMiliseconds;
	}
	
	public void setDurationInMiliseconds(int durationInMiliseconds) {
		this.durationInMiliseconds = durationInMiliseconds;
	}

	private void init() {
		if (getBaseBackgroundColor() == null)
			setBaseBackgroundColor(Color.WHITE);
		if (getPeakBackgroundColor() == null)
			setPeakBackgroundColor(Color.WHITE);
		setBorder(2, Color.GRAY.getHexValue() + " " + Color.BLACK.getHexValue(), "outset");
		DOM.setStyleAttribute(getAnchorElement(), "textDecoration", "none");
		addStyleName("gwt-pulsatinghyperlink");
		timer.scheduleRepeating(TIMER_PERIOD_MILISECONDS);
	}
	
	/**
	 * @param color {@link Color}
	 * @param component int 0=red, 1=green, 2=blue
	 * @return
	 */
	private static final int getComponent(Color color, int component) {
		if (component == 0) return color.getRed();
		if (component == 1) return color.getGreen();
		if (component == 2) return color.getBlue();
		throw new IllegalArgumentException("Invalid color index: " + component);
	}
	
	private final void setBackgroundColor(final Color color) {
		DOM.setStyleAttribute(getAnchorElement(), "backgroundColor", color.getHexValue());
	}
	
	private class PulsateTimer extends Timer {
		private long startTime = new Date().getTime();
		
		public void run() {
			if (getDurationInMiliseconds() < 2)
				return;
			
			int halfDuration = new Float(getDurationInMiliseconds() / 2).intValue();
			long timeLapsed = new Date().getTime() - startTime;
			int span = 0;
			int change = 0;
			
			for (int component = 0; component <= 2; component++) {
				span = getComponent(peakBackgroundColor, component) - getComponent(baseBackgroundColor, component);
				change = new Float(((timeLapsed * span) / halfDuration)).intValue();
				if (goingUp)
					current[component] = getComponent(baseBackgroundColor, component) + change;
				else
					current[component] = getComponent(peakBackgroundColor, component) - change;
			}
			
			if (timeLapsed >= halfDuration) {
				goingUp = !goingUp;
				startTime += halfDuration;
				
				// To see the purpose for these next two lines, uncomment them,
				// put your computer to sleep for a while, and then come back and watch.
				if (new Date().getTime() - startTime > 100)
					startTime = new Date().getTime();
			}
			
			setBackgroundColor(new com.jonosoft.ftpbrowser.web.client.Color(current[0], current[1], current[2]));
		}
	}
}
