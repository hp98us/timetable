package database;

import java.util.*;
import java.sql.*;
import schedule.Faculty;

public class FacultyDB {
	static Connection conn = null;

	public Map<String,Faculty> getAllFaculty() throws SQLException
	{
		//ResultSet rs = null;

		Map<String,Faculty>f=new HashMap<String,Faculty>(50);

		conn = DBconnect.getConn();
		String sql = "select * from Faculty;";

		Statement stm = (Statement) conn.createStatement();
		ResultSet rs = stm.executeQuery(sql);

		while(rs.next()) {
			Faculty fac =new Faculty();
			fac.setFacId(rs.getString(1));
			fac.setFacName(rs.getString(2));
			f.put(fac.getFacId(), fac);
		}
		return f;

	}
}
