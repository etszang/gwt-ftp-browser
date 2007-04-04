/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.URL;

/**
 * @author Jkelling
 *
 */
public class JSRequestBuilder extends RequestBuilder {
	
	private Collection parameterCollection = new Vector();
	
	public JSRequestBuilder(Method httpMethod, String url) {
		super(httpMethod, url);
		if (POST.toString().equals(httpMethod.toString()))
			setHeader("Content-Type", "application/x-www-form-urlencoded");
	}

	public JSRequestBuilder(String httpMethod, String url) {
		super(httpMethod, url);
		if (POST.toString().equals(httpMethod.toString()))
			setHeader("Content-Type", "application/x-www-form-urlencoded");
	}
	
	public void addParameter(String name, String value) {
		parameterCollection.add(new NameValuePair(name, value));
	}
	
	public void addParameter(NameValuePair nvp) {
		parameterCollection.add(nvp);
	}

	public Request sendRequest(RequestCallback callback) throws RequestException {
		return super.sendRequest(getRequestDataAsString(), callback);
	}

	public String getRequestDataAsString() {
		StringBuffer sb = new StringBuffer();
		for (Iterator it = parameterCollection.iterator(); it.hasNext();) {
			if (sb.length() > 0)
				sb.append("&");
			sb.append(((NameValuePair) it.next()).toString(true));
		}
		return sb.toString();
	}
	
	public static class NameValuePair {
		private String name = null;
		private String value = null;
		
		public NameValuePair(String name, String value) {
			this.name = name;
			this.value = value;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getValue() {
			return value;
		}
		
		public void setValue(String value) {
			this.value = value;
		}
		
		public String toString() {
			return toString(true);
		}
		
		public String toString(boolean urlEncode) {
			if (name == null || name.trim().length() == 0)
				return "";
			return new StringBuffer()
				.append(urlEncode ? URL.encodeComponent(name) : name)
				.append("=")
				.append(urlEncode ? URL.encodeComponent(emptyIfNull(value)) : emptyIfNull(value))
			.toString();
		}
		
		public boolean equals(Object o) {
			if (o instanceof NameValuePair) {
				NameValuePair nvp = (NameValuePair) o;
				return emptyIfNull(name).equals(emptyIfNull(nvp.getName()))
					&& emptyIfNull(value).equals(emptyIfNull(nvp.getValue()));
			}
			return false;
		}
		
		public int hashCode() {
			return (emptyIfNull(name) + emptyIfNull(value)).hashCode();
		}
		
		private static final String emptyIfNull(String s) {
			return (s == null) ? "" : s;
		}
	}
	
}
