package com.jonosoft.ftpbrowser.web.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import sun.net.ftp.FtpClient;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.jonosoft.ftpbrowser.web.client.FTPBrowserFatalException;
import com.jonosoft.ftpbrowser.web.client.FTPFileGroup;
import com.jonosoft.ftpbrowser.web.client.FTPFileGroupItem;
import com.jonosoft.ftpbrowser.web.client.FTPFileItem;
import com.jonosoft.ftpbrowser.web.client.FTPIOException;
import com.jonosoft.ftpbrowser.web.client.FTPService;
import com.jonosoft.ftpbrowser.web.client.FTPSite;
import com.jonosoft.ftpbrowser.web.server.data.FTPFileGroupDAO;
import com.jonosoft.ftpbrowser.web.server.data.FTPFileGroupItemDAO;
import com.jonosoft.ftpbrowser.web.server.data.FTPSiteDAO;

public class FTPServiceImpl extends RemoteServiceServlet implements FTPService {

	private static final Logger LOGGER = LogManager.getLogger(FTPServiceImpl.class);

	private static final Pattern PATTERN_SPLIT_BY_NEWLINE = Pattern.compile("[\\r\\n]{1,2}");
	private static final Pattern PATTERN_SPLIT_BY_WHITESPACE = Pattern.compile("\\s+");

	protected static Integer getUserId(FTPServiceImpl sender) {
		return new Integer(7);
	}

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
			LOGGER.error("FtpClient threw an IOException trying to list contents at path: " + path + "; for FTPSite: " + site.toString(), e);
			throw new FTPIOException("FtpClient threw an IOException trying to list contents at path: " + path + "; for FTPSite: " + site.toString());
		}
		finally {
			new Thread(new Runnable() {
				public void run() {
					try {
						client.closeServer();
					} catch (IOException e) {
					}
				}
			}).start();
		}
	}


	public List getUserFTPSites() throws FTPBrowserFatalException {
		try {
			return new FTPSiteDAO().findByUserId(getUserId(this));
		}
		finally {
			HibernateSessionFactory.closeSession();
		}
	}

	public FTPSite saveUserFTPSite(FTPSite site) throws FTPBrowserFatalException {
		try {
			HibernateSessionFactory.getSession().beginTransaction();
			site.setUserId(getUserId(this));
			new FTPSiteDAO().attachDirty(site);
			HibernateSessionFactory.getSession().getTransaction().commit();
			HibernateSessionFactory.closeSession();
			return site;
		}
		catch (RuntimeException e) {
			LOGGER.fatal(e);
			throw e;
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
			HibernateSessionFactory.closeSession();
		}
	}

	public List getUserFTPFileItems(Integer groupId) throws FTPBrowserFatalException {
		try {
			HibernateSessionFactory.getSession().beginTransaction();
			FTPFileGroupItemDAO dao = new FTPFileGroupItemDAO();
			List list = dao.findByFtpFileGroupId(getDefaultGroupId());
			HibernateSessionFactory.getSession().getTransaction().commit();
			return list;
		}
		catch (RuntimeException e) {
			LOGGER.fatal(e);
			throw e;
		}
		finally {
			HibernateSessionFactory.closeSession();
		}
	}

	private Integer getDefaultGroupId() {
		FTPFileGroupDAO groupDAO = new FTPFileGroupDAO();
		FTPFileGroup group = new FTPFileGroup();
		group.setName("Default");
		group.setUserId(getUserId(this));

		List groups = groupDAO.findByExample(group);

		if (groups.size() == 0)
			groupDAO.attachDirty(group);
		else
			group = (FTPFileGroup) groups.get(0);

		return group.getFtpFileGroupId();
	}

	public void saveUserFTPFileItems(Integer groupId, List ftpFileItems) throws FTPBrowserFatalException {
		try {
			HibernateSessionFactory.getSession().beginTransaction();

			final FTPFileGroupItemDAO dao = new FTPFileGroupItemDAO();
			FTPFileItem fileItem;
			FTPFileGroupItem fileGroupItem;
			FTPFileGroupItem fileGroupItemToSave;
			List list = null;
			final Map savedItems = new HashMap();
			final Map existingItems = new HashMap();

			groupId = getDefaultGroupId();

			for (Iterator it = dao.findByFtpFileGroupId(groupId).iterator(); it.hasNext();) {
				fileGroupItem = (FTPFileGroupItem) it.next();
				existingItems.put(fileGroupItem.getFtpFileGroupItemId(), fileGroupItem);
			}

			LOGGER.debug("Using default groupId: " + groupId);

			for (Iterator it = ftpFileItems.iterator(); it.hasNext();) {
				fileItem = (FTPFileItem) it.next();
				fileGroupItem = new FTPFileGroupItem();

				LOGGER.debug("Current iterator FTPFileItem:\n" + fileItem);

				fileGroupItem.setFtpFileGroupId(groupId);
				fileGroupItem.setFtpSiteId(fileItem.getFtpSiteId());
				fileGroupItem.setFullPath(fileItem.getFullPath());
				fileGroupItem.setName(fileItem.getName());
				fileGroupItem.setType(fileItem.getType());

				LOGGER.debug("About to findByExample with FTPFileGroupItem:\n" + fileGroupItem);

				list = dao.findByExample(fileGroupItem);

				if (list.size() > 1)
					LOGGER.warn("saveUserFTPFileItems: FTPFileGroupItemDAO.findByExample(...) should return only 0 or 1 results. Results returned: " + list.size());

				LOGGER.debug("findByExample result count: " + list.size());

				if (list.size() == 0)
					fileGroupItemToSave = fileGroupItem;
				else
					fileGroupItemToSave = (FTPFileGroupItem) list.get(0);

				LOGGER.debug("About to save FTPFileGroupItem:\n" + fileGroupItemToSave);

				dao.attachDirty(fileGroupItemToSave);

				savedItems.put(fileGroupItemToSave.getFtpFileGroupItemId(), fileGroupItemToSave);
			}

			/*LOGGER.debug("Saved Items:");
			for (Iterator it = savedItems.keySet().iterator(); it.hasNext();) {
				Integer key = (Integer) it.next();
				LOGGER.debug(key + ": " + ((FTPFileItem) savedItems.get(key)).getFullPath());
			}

			LOGGER.debug("\n");
			LOGGER.debug("Existing:");
			for (Iterator it = existingItems.keySet().iterator(); it.hasNext();) {
				Integer key = (Integer) it.next();
				LOGGER.debug(key + ": " + ((FTPFileGroupItem) existingItems.get(key)).getFullPath());
			}
			LOGGER.debug("\n");*/

			for (Iterator it = existingItems.keySet().iterator(); it.hasNext();) {
				Integer i = (Integer) it.next();
				if (! savedItems.containsKey(i))
					dao.delete((FTPFileItem) existingItems.get(i));
			}

			HibernateSessionFactory.getSession().getTransaction().commit();
		}
		finally {
			HibernateSessionFactory.closeSession();
		}
	}
}
