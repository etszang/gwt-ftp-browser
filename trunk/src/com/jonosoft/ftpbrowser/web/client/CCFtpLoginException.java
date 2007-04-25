/**
 * 
 */
package com.jonosoft.ftpbrowser.web.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Jkelling
 *
 */
public class CCFtpLoginException extends Exception implements IsSerializable {

	public CCFtpLoginException() {
		super();
	}

	public CCFtpLoginException(String message, Throwable cause) {
		super(message, cause);
	}

	public CCFtpLoginException(String message) {
		super(message);
	}

	public CCFtpLoginException(Throwable cause) {
		super(cause);
	}

}
