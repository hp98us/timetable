package database;

import java.sql.*;

public class DBconnect {

	public static Connection getConn()
	{
		Connection conn = null;

		try {
			Class.forName(DBstatic.JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(DBstatic.DB_URL,DBstatic.USER,DBstatic.PASS);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	return conn;
	}
}
