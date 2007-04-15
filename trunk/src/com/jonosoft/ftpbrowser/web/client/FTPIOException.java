/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * @author Jkelling
 *
 */
public class FTPIOException extends Exception implements IsSerializable {
	
	public FTPIOException() {
		super();
	}

	public FTPIOException(Throwable e) {
		super(e);
	}
	
	public FTPIOException(String msg) {
		super(msg);
	}
	
	public FTPIOException(String msg, Throwable e) {
		super(msg, e);
	}

}
