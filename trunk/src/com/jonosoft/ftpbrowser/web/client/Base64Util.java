/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;


/**
 * This was written primarily for sending JSON via HTTP post request
 * 
 * @author jon
 */
public class Base64Util {

	private static final String keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_.-"; //+/=

	// TODO Add unit test for kicks
	/*public static void main(String[] args) {
		final String [] test = new String[] {
				"carnal pleasure.",
				"carnal pleasure",
				"carnal pleasur",
				"carnal pleasu"
		};

		for (int i = 0; i < test.length; i++) {
			String a = encode(test[i]);
			String b = decode(a);
			System.out.println("Test #" + Integer.toString(i) + ": " + Boolean.toString(a.equals(b)));
			System.out.println("        " + a);
			System.out.println("        " + b);
		}
	}*/

	public static String encode(String input) {
		StringBuffer output = new StringBuffer();
		char c1=0, c2=0, c3=0;
		boolean f2 = true, f3 = true;
		int i = 0;

		input = utf8Encode(input);

		while (i < input.length()) {
			c1 = input.charAt(i++);
			try {
				c2 = input.charAt(i++);
				try {
					c3 = input.charAt(i++);
				}
				catch (StringIndexOutOfBoundsException ignored) {
					c3 = 0;
					f3 = false;
				}
			}
			catch (StringIndexOutOfBoundsException ignored) {
				c2 = c3 = 0;
				f2 = f3 = false;
			}

			output.append(keyStr.charAt((char) (c1 >> 2)));
			output.append(keyStr.charAt((char) (((c1 & 3) << 4) | (c2 >> 4))));
			if (f2)
				output.append(keyStr.charAt((char) (((c2 & 15) << 2) | (c3 >> 6))));
			else
				output.append(keyStr.charAt(64));
			if (f3)
				output.append(keyStr.charAt((char) (c3 & 63)));
			else
				output.append(keyStr.charAt(64));
		}

		return output.toString();
	}

	public static String decode(String input) {
		StringBuffer output = new StringBuffer();
		char chr1=0, chr2=0, chr3=0;
		char enc1=0, enc2=0, enc3=0, enc4=0;
		int i = 0;

		input = input.replaceAll("[^A-Za-z0-9\\*\\-\\=\\+\\/]", "");

		while (i < input.length()) {
			enc1 = (char) keyStr.indexOf(input.charAt(i++));
			enc2 = (char) keyStr.indexOf(input.charAt(i++));
			enc3 = (char) keyStr.indexOf(input.charAt(i++));
			enc4 = (char) keyStr.indexOf(input.charAt(i++));

			chr1 = (char) ((enc1 << 2) | (enc2 >> 4));
			chr2 = (char) (((enc2 & 15) << 4) | (enc3 >> 2));
			chr3 = (char) (((enc3 & 3) << 6) | enc4);

			output.append(String.valueOf(chr1));

			if (enc3 != 64)
				output.append(String.valueOf(chr2));
			if (enc4 != 64)
				output.append(String.valueOf(chr3));
		}

		return utf8Decode(output.toString());
	}

	public static String utf8Encode(String input) {
		StringBuffer utftxt = new StringBuffer();
		char c;

		input = input.replaceAll("\\r\\n", String.valueOf((char) 13));

		for (int i = 0; i < input.length(); i++) {
			c = input.charAt(i);

			if (c < 128) {
				utftxt.append(String.valueOf(c));
			}
			else if ((c < 127) && (c < 2048)) {
				utftxt.append(String.valueOf((char)((c >> 6) | 192)));
				utftxt.append(String.valueOf((char)((c & 63) | 128)));
			}
			else {
				utftxt.append(String.valueOf((char)((c >> 12) | 224)));
				utftxt.append(String.valueOf((char)(((c >> 6) & 63) | 128)));
				utftxt.append(String.valueOf((char)((c & 63) | 128)));
			}
		}

		return utftxt.toString();
	}

	public static String utf8Decode(String utftxt) {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		char c, c2, c3;

		while (i < utftxt.length()) {
			c = utftxt.charAt(i);

			if (c < 128) {
				sb.append(String.valueOf(c));
				i += 1;
			}
			else if((c > 191) && (c < 224)) {
				c2 = utftxt.charAt(i+1);
				sb.append(String.valueOf((char)(((c & 31) << 6) | (c2 & 63))));
				i += 2;
			}
			else {
				c2 = utftxt.charAt(i+1);
				c3 = utftxt.charAt(i+2);
				sb.append(String.valueOf((char)(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63))));
				i += 3;
			}
		}

		return sb.toString();
	}
}
