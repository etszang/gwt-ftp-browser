/**
 * 
 */
package com.jonosoft.ftpbrowser.web.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jonosoft.ftpbrowser.web.client.FTPBrowserFatalException;
import com.jonosoft.ftpbrowser.web.client.FTPSite;

/**
 * @author Jkelling
 *
 */
public class FTPSiteDB extends ObjectDB {
	
	private static FTPSite build(ResultSet rs) throws SQLException {
		FTPSite site = new FTPSite();
		
		site.setFtpSiteId(getInt(rs, "ftp_site_id"));
		site.setUserId(getInt(rs, "user_id"));
		site.setHost(rs.getString("server"));
		site.setPort(getInt(rs, "port"));
		site.setUsername(rs.getString("username"));
		site.setPassword(rs.getString("password"));
		
		return site;
	}
	
	private static List buildAll(ResultSet rs) throws SQLException {
		List list = new ArrayList();
		while (rs.next())
			list.add(build(rs));
		return list;
	}
	
	public static List retrieveForUserID(DBConnection dbConnection, int userId) throws FTPBrowserFatalException {
		final String sql;
		final Connection conn;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		sql = "select * from ftp_site where user_id = ?";
		
		try {
			conn = dbConnection.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.execute();
			rs = ps.getResultSet();
			return buildAll(rs);
		}
		catch (SQLException e) {
			throw new FTPBrowserFatalException(e.getMessage());
		}
		finally {
			cleanup(ps, rs);
		}
	}
	
	public static FTPSite save(DBConnection dbConnection, FTPSite site) throws FTPBrowserFatalException {
		if (site != null) {
			try {
				if (site.getFtpSiteId() == null || site.getFtpSiteId().intValue() == 0)
					return insert(dbConnection, site);
				else
					return update(dbConnection, site);
			}
			catch (SQLException e) {
				throw new FTPBrowserFatalException("Error saving FTPSite: " + site.toString() + "; Error Message: " + e.getMessage());
			}
		}
		return null;
	}
	
	private static FTPSite insert(DBConnection dbConnection, FTPSite site) throws SQLException {
		final String sql;
		final Connection conn;
		PreparedStatement ps = null;
		
		sql = "insert into ftp_site (user_id,server,port,username,password) values (?,?,?,?,?)";
		
		try {
			conn = dbConnection.getConnection();
			ps = conn.prepareStatement(sql);
			
			setInt(ps, 1, site.getUserId());
			ps.setString(2, site.getHost());
			setInt(ps, 3, site.getPort());
			ps.setString(4, site.getUsername());
			ps.setString(5, site.getPassword());
			
			ps.executeUpdate();
			
			site.setFtpSiteId(new Integer(StatementLastInsertId.executeGetLastInsertId(dbConnection)));
			
			return site;
		}
		finally {
			cleanup(ps);
		}
	}
	
	private static FTPSite update(DBConnection dbConnection, FTPSite site) throws SQLException {
		final String sql;
		final Connection conn;
		PreparedStatement ps = null;
		
		sql = "update ftp_site set user_id=?,server=?,port=?,username=?,password=? where ftp_site_id=?";
		
		try {
			conn = dbConnection.getConnection();
			ps = conn.prepareStatement(sql);
			
			setInt(ps, 1, site.getUserId());
			ps.setString(2, site.getHost());
			setInt(ps, 3, site.getPort());
			ps.setString(4, site.getUsername());
			ps.setString(5, site.getPassword());
			setInt(ps, 6, site.getFtpSiteId());
			
			ps.executeUpdate();
			
			return site;
		}
		finally {
			cleanup(ps);
		}
	}
	
	public static void delete(DBConnection dbConnection, FTPSite site) throws FTPBrowserFatalException {
		final String sql;
		final Connection conn;
		PreparedStatement ps = null;
		
		sql = "delete from ftp_site where ftp_site_id=?";
		
		try {
			conn = dbConnection.getConnection();
			ps = conn.prepareStatement(sql);
			
			setInt(ps, 1, site.getFtpSiteId());
			
			ps.executeUpdate();
		}
		catch (SQLException e) {
			throw new FTPBrowserFatalException("Error deleting FTPSite: " + site.toString() + "; Error Message: " + e.getMessage()); 
		}
		finally {
			cleanup(ps);
		}
	}
	
}
