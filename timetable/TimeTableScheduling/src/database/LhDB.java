package database;

import java.sql.*;
import java.util.Vector;
import schedule.LectureHall;

public class LhDB {
	static Connection conn = null;

	public Vector<LectureHall> getAllLH() throws SQLException{
		
		ResultSet rs = null;

		Vector<LectureHall>l=new Vector<LectureHall>(50);

		conn = DBconnect.getConn();
		String sql = "select * from LectureHall;";

		Statement stm = (Statement) conn.createStatement();
		rs = stm.executeQuery(sql);

		while(rs.next()) {
			LectureHall lh = new LectureHall();
			lh.setLhId(rs.getInt(1));
			lh.setCapacity(rs.getInt(2));
			//add course to vector
			l.add(lh);
		}
		return l;

	}
}
