/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;


/**
 * @author Jkelling
 *
 */
public class FTPIOException extends Exception {

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
