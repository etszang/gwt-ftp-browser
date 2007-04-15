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
		
		site.setFtpSiteId(rs.getInt("ftp_site_id"));
		site.setHost(rs.getString("server"));
		site.setPort(rs.getInt("port"));
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
	
}
