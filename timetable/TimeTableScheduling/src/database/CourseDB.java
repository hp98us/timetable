package database;

import java.sql.*;
import java.util.*;

import schedule.Course;

public class CourseDB {
	static Connection conn = null;

	public Vector<Course> getALLCourse() throws SQLException
	{
		ResultSet rs = null;

		Vector<Course>c=new Vector<Course>(50);

		conn = DBconnect.getConn();
		String sql = "select * from Course;";

		Statement stm = (Statement) conn.createStatement();
		rs = stm.executeQuery(sql);

		while(rs.next()) {
			Course cr = new Course();
			cr.setCrsId(rs.getString(1));
			cr.setCrsName(rs.getString(2));
			cr.setfId(rs.getString(3));
			cr.setCrsStrength(rs.getInt(4));
			//add course to vector
			c.add(cr);
		}
		return c;

	}
}
