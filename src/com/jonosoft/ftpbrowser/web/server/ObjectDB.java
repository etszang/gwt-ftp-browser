/**
 * 
 */
package com.jonosoft.ftpbrowser.web.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Jkelling
 *
 */
public class ObjectDB {

	protected static void cleanup(Statement stmt) {
		cleanup(stmt, null);
	}
	
	protected static void cleanup(Statement stmt, ResultSet rs) {
		try {
			if (stmt != null)
				stmt.close();
			if (rs != null)
				rs.close();
		}
		catch (SQLException ignoredException) {
		}
	}
	
	protected static final void setInt(PreparedStatement ps, int parameterIndex, Integer i) throws SQLException {
		ps.setInt(parameterIndex, (i == null) ? 0 : i.intValue());
	}
	
	protected static final Integer getInt(ResultSet rs, String columnName) throws SQLException {
		return new Integer(rs.getInt(columnName));
	}

}
