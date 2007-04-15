/**
 * 
 */
package com.jonosoft.ftpbrowser.web.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jkelling
 *
 */
public class StatementLastInsertId {
	
	/**
	 * @param dbConnection
	 * @return int Last inserted Id or -1 if none was found
	 * @throws SQLException
	 */
	public static int executeGetLastInsertId(DBConnection dbConnection) throws SQLException {
		final String sql = "select last_insert_id()";
		final Connection conn;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = dbConnection.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			if (rs.next())
				return rs.getInt(1);
		}
		finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		
		return -1;
	}
	
}
