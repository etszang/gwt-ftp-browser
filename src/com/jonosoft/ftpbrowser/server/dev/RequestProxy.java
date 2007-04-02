package com.jonosoft.ftpbrowser.server.dev;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * TODO Add description PHPProxy (com.macroleads.server.web.rpc.PHPProxy)
 *
 * @author jon
 *
 */
public class RequestProxy implements Filter {
	private static final String UTF8 = "UTF-8";

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
	        FilterChain chain) throws IOException, ServletException {
		forward((HttpServletRequest) request, response);
	}

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig arg0) throws ServletException {
	}
	
	private void forward(HttpServletRequest request, ServletResponse response) throws IOException {
		final HttpClient httpClient = new HttpClient();
		final String url = "http://www.cookiecloaker.com" + request.getRequestURI().replaceFirst("/gwt-ftp-browser", "");
		HttpMethodBase method = null;
    	
    	if (request.getMethod().equalsIgnoreCase("POST")) {
    		method = new PostMethod(url);
    		
    		final BufferedReader br = request.getReader();
    		final StringBuffer postData = new StringBuffer();
    		
    		for (int i = 0; i < request.getContentLength(); i++)
    			postData.append((char) br.read());
    		
    		//((PostMethod) method).addParameter("json", URLEncoder.encode(postData.toString(), UTF8));
    		
    		String[] sp = postData.toString().split("[&=]");
    		
    		for (int i = 0; i < sp.length; i+=2) {
    			((PostMethod) method).addParameter(URLEncoder.encode(sp[i], UTF8), URLEncoder.encode(sp[i+1], UTF8));
    		}
		} else {
			System.out.println(url + "?" + request.getQueryString());
    		method = new GetMethod(url + "?" + request.getQueryString());
    	}
    	
	    httpClient.executeMethod(method);
	    
	    final InputStream is = method.getResponseBodyAsStream();
	    int x;
	    
	    while ((x = is.read()) != -1)
	    	response.getOutputStream().print((char)x);
	    
	    method.releaseConnection();
    }

}
