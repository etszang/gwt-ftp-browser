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
import com.jonosoft.ftpbrowser.web.client.FTPFileGroup;

/**
 * @author Jkelling
 *
 */
public class FTPFileGroupDB extends ObjectDB {
	
	private static FTPFileGroup build(ResultSet rs) throws SQLException {
		FTPFileGroup group = new FTPFileGroup();
		
		group.setGroupId(rs.getInt("ftp_file_group_id"));
		group.setUserId(rs.getInt("user_id"));
		group.setName(rs.getString("name"));
		
		return group;
	}
	
	private static List buildAll(ResultSet rs) throws SQLException {
		List list = new ArrayList();
		while (rs.next())
			list.add(build(rs));
		return list;
	}
	
	public static FTPFileGroup save(DBConnection dbConnection, FTPFileGroup fileGroup) throws FTPBrowserFatalException {
		if (fileGroup != null) {
			try {
				if (fileGroup.getGroupId() == 0)
					return insert(dbConnection, fileGroup);
				else
					return update(dbConnection, fileGroup);
			}
			catch (SQLException e) {
				throw new FTPBrowserFatalException("Error saving FTP file group\n\nMessage:\n" + e.getMessage());
			}
		}
		return null;
	}

	private static FTPFileGroup update(DBConnection dbConnection, FTPFileGroup fileGroup) throws SQLException {
		final String sql;
		final Connection conn;
		PreparedStatement ps = null;
		
		sql = "update ftp_file_group set user_id=?,name=? where ftp_file_group_id=?";
		
		try {
			conn = dbConnection.getConnection();
			ps = conn.prepareStatement(sql);
			
			ps.setInt(1, fileGroup.getUserId());
			ps.setString(2, fileGroup.getName());
			ps.setInt(3, fileGroup.getGroupId());
			
			ps.executeUpdate();
			
			return fileGroup;
		}
		finally {
			cleanup(ps);
		}
	}

	private static FTPFileGroup insert(DBConnection dbConnection, FTPFileGroup fileGroup) throws SQLException {
		final String sql;
		final Connection conn;
		PreparedStatement ps = null;
		
		sql = "insert into ftp_file_group (user_id,name) values (?,?)";
		
		try {
			conn = dbConnection.getConnection();
			ps = conn.prepareStatement(sql);
			
			ps.setInt(1, fileGroup.getUserId());
			ps.setString(2, fileGroup.getName());
			
			ps.executeUpdate();
			
			fileGroup.setGroupId(StatementLastInsertId.executeGetLastInsertId(dbConnection));
			
			return fileGroup;
		}
		finally {
			cleanup(ps);
		}
	}
	
	public static void delete(DBConnection dbConnection, FTPFileGroup fileGroup) throws FTPBrowserFatalException {
		final String sql;
		final Connection conn;
		PreparedStatement ps = null;
		
		sql = "delete from ftp_file_group where group_id=?";
		
		try {
			conn = dbConnection.getConnection();
			ps = conn.prepareStatement(sql);
			
			ps.setInt(1, fileGroup.getGroupId());
		}
		catch (SQLException e) {
			throw new FTPBrowserFatalException("Error deleting FTPFileGroup: " + fileGroup.toString() + "; Error Message: " + e.getMessage());
		}
		finally {
			cleanup(ps);
		}
	}
}
