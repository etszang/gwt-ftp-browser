/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Jkelling
 *
 */
public class FTPBrowserFatalException extends Exception implements IsSerializable {

	public FTPBrowserFatalException() {
		super();
	}

	public FTPBrowserFatalException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public FTPBrowserFatalException(String arg0) {
		super(arg0);
	}

	public FTPBrowserFatalException(Throwable arg0) {
		super(arg0);
	}

}
