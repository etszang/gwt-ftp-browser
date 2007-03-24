package com.jonosoft.ftpbrowser.web.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sun.net.ftp.FtpClient;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.jonosoft.ftpbrowser.web.client.FTPConnection;
import com.jonosoft.ftpbrowser.web.client.FTPIOException;
import com.jonosoft.ftpbrowser.web.client.FTPService;

public class FTPServiceImpl extends RemoteServiceServlet implements FTPService {
	
	/**
	 * @param conn
	 * @param path
	 * @return
	 * @throws FTPIOException
	 */
	public List getDirectoryContents(FTPConnection conn, String path) throws FTPIOException {
		FtpClient client = new FtpClient();
		
		try {
			client.openServer(conn.getServer());
			client.login(conn.getUsername(), conn.getPassword());
			
			client.cd(path);
			
			BufferedInputStream bs = new BufferedInputStream(client.list());
			
			int i;
			final StringBuffer sb = new StringBuffer();
			final List contentsList = new ArrayList();
			
			while ((i = bs.read()) != -1)
				sb.append((char) i);
			
			return contentsList;
		}
		catch (IOException e) {
			throw new FTPIOException(e);
		}
	}
	
}
