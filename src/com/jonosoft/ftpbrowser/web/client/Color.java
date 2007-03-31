/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;


/**
 * @author Jkelling
 *
 */
public class Color extends org.gwtwidgets.client.style.Color {
	
	private static final String PARSE_COLOR_TEXT_REGEXP_1 = "^rgb\\s*\\(\\s*(\\d+?)\\s*,\\s*?(\\d+?)\\s*,\\s*?(\\d+?)\\s*\\)$";
	private static final String PARSE_COLOR_TEXT_REGEXP_2 = "^#([a-f0-9]{1,2})([a-f0-9]{1,2})([a-f0-9]{1,2})$";
	
	/**
	 * @param r
	 * @param g
	 * @param b
	 */
	public Color(int r, int g, int b) {
		super(limitTo0or255(r), limitTo0or255(g), limitTo0or255(b));
	}
	
	public Color(float r, float g, float b) {
		this(new Float(r).intValue(), new Float(g).intValue(), new Float(b).intValue());
	}
	
	private static final int limitTo0or255(final int value) {
		return (value < 0) ? 0 : (value > 255 ? 255: value);
	}
	
	public static Color parseColor(String colorText) {
		colorText = colorText.trim().toLowerCase();
		
		if (colorText.matches(PARSE_COLOR_TEXT_REGEXP_1)) {
			return new Color(
				Integer.parseInt(colorText.replaceFirst(PARSE_COLOR_TEXT_REGEXP_1, "$1")),
				Integer.parseInt(colorText.replaceFirst(PARSE_COLOR_TEXT_REGEXP_1, "$2")),
				Integer.parseInt(colorText.replaceFirst(PARSE_COLOR_TEXT_REGEXP_1, "$3")));
		}
		else if (colorText.matches(PARSE_COLOR_TEXT_REGEXP_2)) {
			return new Color(
				Integer.parseInt(colorText.replaceFirst(PARSE_COLOR_TEXT_REGEXP_2, "$1"), 16),
				Integer.parseInt(colorText.replaceFirst(PARSE_COLOR_TEXT_REGEXP_2, "$2"), 16),
				Integer.parseInt(colorText.replaceFirst(PARSE_COLOR_TEXT_REGEXP_2, "$3"), 16));
		}
		else {
			throw new IllegalArgumentException("Invalid colorText value. Should be in either of the following formats: (1) \"#A1B2C3\" or (2) \"rgb(123, 234, 102)\"");
		}
	}

}
