package com.jonosoft.ftpbrowser.web.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import sun.net.ftp.FtpClient;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.jonosoft.ftpbrowser.web.client.FTPBrowserFatalException;
import com.jonosoft.ftpbrowser.web.client.FTPFileItem;
import com.jonosoft.ftpbrowser.web.client.FTPIOException;
import com.jonosoft.ftpbrowser.web.client.FTPService;
import com.jonosoft.ftpbrowser.web.client.FTPSite;
import com.jonosoft.ftpbrowser.web.server.data.FTPSiteDAO;

public class FTPServiceImpl extends RemoteServiceServlet implements FTPService {
	
	private static final Pattern PATTERN_SPLIT_BY_NEWLINE = Pattern.compile("[\\r\\n]{1,2}");
	private static final Pattern PATTERN_SPLIT_BY_WHITESPACE = Pattern.compile("\\s+");
	
	public List getFileList(FTPSite site, String path) throws FTPIOException {
		final FtpClient client = new FtpClient();
		final StringBuffer sb;
		final List fileList;
		final String[] lines;
		String[] items;
		int i;
		
		try {
			client.openServer(site.getHost());
			client.login(site.getUsername(), site.getPassword());
			
			client.cd(path);
			
			//
			// Read FTP response from input stream, which should look something like this...
			//   drwxr-xr-x  12 user group     4096 Apr  9 00:20 .
			//   drwxr-xr-x  12 user group     4096 Apr  9 00:20 ..
			//   -rw-r--r--   1 user group   152018 Apr  9 00:19 10321CC20DC45A7DB05C03B200A0422E.cache.html
			//   -rw-r--r--   1 user group     1566 Apr  9 00:19 10321CC20DC45A7DB05C03B200A0422E.cache.xml
			//
			
			BufferedInputStream bs = new BufferedInputStream(client.list());
			sb = new StringBuffer();
			while ((i = bs.read()) != -1)
				sb.append((char) i);
			
			//
			// Parse contents into FTPFileItem instances
			//
			
			fileList = new ArrayList();
			lines = PATTERN_SPLIT_BY_NEWLINE.split(sb.toString());
			
			for (i = 0; i < lines.length; i++) {
				items = PATTERN_SPLIT_BY_WHITESPACE.split(lines[i]);
				fileList.add(new FTPFileItem(site, items[8], items[0].startsWith("d") ? "d" : "f", path));
			}
			
			return fileList;
		} catch (IOException e) {
			throw new FTPIOException("FtpClient threw an IOException trying to list contents at path: " + path + "; for FTPSite: " + site.toString());
		}
		finally {
			try {
				client.closeServer();
			} catch (IOException e) {
				// TODO Log this
			}
		}
	}
	

	public List getUserFTPSites(Integer userId) throws FTPBrowserFatalException {
		DBConnection conn = null;

		try {
			/*conn = new FTPBrowserDBConnection();
			return FTPSiteDB.retrieveForUserID(conn, userId);*/
			
			FTPSiteDAO dao = new FTPSiteDAO();
			
			List list = dao.findByUserId(userId);
			
			HibernateSessionFactory.closeSession();
			
			return list;
		}
		/*catch (FTPBrowserFatalException e) {
			System.out.println(e.toString());
			throw e;
		}*/
		finally {
			cleanup(conn);
		}
	}

	public FTPSite saveUserFTPSite(FTPSite site) throws FTPBrowserFatalException {
		DBConnection conn = null;
		
		try {
			/*conn = new FTPBrowserDBConnection();
			FTPSite retSite = FTPSiteDB.save(conn, site);*/
			
			FTPSiteDAO dao = new FTPSiteDAO();
			
			HibernateSessionFactory.getSession().beginTransaction();
			
			dao.save(site);
			
			HibernateSessionFactory.getSession().getTransaction().commit();
			HibernateSessionFactory.closeSession();
			
			//auto-commit is on
			//conn.getConnection().commit();
			//return retSite;
			return site;
		}
		/*catch (SQLException e) {
			throw new FTPBrowserFatalException("Error committing record");
		}*/
		finally {
			cleanup(conn);
		}
	}
	
	public void deleteUserFTPSite(FTPSite site) throws FTPBrowserFatalException {
		DBConnection conn = null;
		
		try {
			/*conn = new FTPBrowserDBConnection();
			FTPSiteDB.delete(conn, site);*/
			/*FTPSiteManager mgr = new FTPSiteManager();
			
			mgr.delete(site);
			
			HibernateUtil.getSessionFactory().close();*/
			
			FTPSiteDAO dao = new FTPSiteDAO();
			
			HibernateSessionFactory.getSession().beginTransaction();
			
			dao.delete(site);
			
			HibernateSessionFactory.getSession().getTransaction().commit();
			HibernateSessionFactory.closeSession();
		}
		/*catch (FTPBrowserFatalException e) {
			System.out.println(e.toString());
			throw e;
		}*/
		finally {
			cleanup(conn);
		}
	}
	
	private int getUserId() {
		return 7;
	}
	
	protected void cleanup(DBConnection conn) {
		try {
			if (conn != null)
				conn.closeConnection();
		}
		catch (SQLException ignoredException) {
		}
	}
}
