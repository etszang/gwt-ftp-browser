package com.jonosoft.ftpbrowser.web.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import sun.net.ftp.FtpClient;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.jonosoft.ftpbrowser.web.client.FTPBrowserFatalException;
import com.jonosoft.ftpbrowser.web.client.FTPFileGroup;
import com.jonosoft.ftpbrowser.web.client.FTPFileItem;
import com.jonosoft.ftpbrowser.web.client.FTPIOException;
import com.jonosoft.ftpbrowser.web.client.FTPService;
import com.jonosoft.ftpbrowser.web.client.FTPSite;
import com.jonosoft.ftpbrowser.web.server.data.FTPFileGroupDAO;
import com.jonosoft.ftpbrowser.web.server.data.FTPFileGroupItemDAO;
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
				fileList.add(new FTPFileItem(site.getFtpSiteId() , items[8], items[0].startsWith("d") ? "d" : "f", path));
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
	

	public List getUserFTPSites() throws FTPBrowserFatalException {
		try {
			return new FTPSiteDAO().findByUserId(getUserId());
		}
		finally {
//			HibernateSessionFactory.closeSession();
		}
	}

	public FTPSite saveUserFTPSite(FTPSite site) throws FTPBrowserFatalException {
		try {
			HibernateSessionFactory.getSession().beginTransaction();
			new FTPSiteDAO().attachDirty(site);
			HibernateSessionFactory.getSession().getTransaction().commit();
			return site;
		}
		finally {
//			HibernateSessionFactory.closeSession();
		}
	}
	
	public void deleteUserFTPSite(FTPSite site) throws FTPBrowserFatalException {
		
		try {
			FTPSiteDAO dao = new FTPSiteDAO();
			
			HibernateSessionFactory.getSession().beginTransaction();
			
			dao.delete(site);
			
			HibernateSessionFactory.getSession().getTransaction().commit();
		}
		finally {
			//HibernateSessionFactory.closeSession();
		}
	}
	
	public List getUserFTPFileItems(Integer groupId) throws FTPBrowserFatalException {
		try {
			HibernateSessionFactory.getSession().beginTransaction();
			
			FTPFileGroupDAO groupDAO = new FTPFileGroupDAO();
			FTPFileGroup group = new FTPFileGroup();
			group.setName("Default");
			group.setUserId(getUserId());
			
			List groups = groupDAO.findByExample(group);
			
			if (groups.size() == 0) {
				groupDAO.attachDirty(group);
			}
			else {
				group = (FTPFileGroup) groups.get(0);
			}
			
			FTPFileGroupItemDAO dao = new FTPFileGroupItemDAO();
			
			List list = dao.findByFtpFileGroupId(group.getFtpFileGroupId());
			
			HibernateSessionFactory.getSession().getTransaction().commit();
			
			return list;
		}
		catch (RuntimeException e) {
			System.out.println(e.toString());
			throw e;
		}
		finally {
			//HibernateSessionFactory.closeSession();
		}
		
	}
	
	protected Integer getUserId() {
		return new Integer(7);
	}
}
