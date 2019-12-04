package org.horn.mommons.dao.hvie;

import java.sql.*;

public class HiveClient {
	public void readHiveTable() throws SQLException {
		Connection con = connection("jdbc:hive://192.168.2.100:10000/default", "bx100", "beixin*K8");
		String sql = "select name,count(*) num from table1 group by name";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {
			String name = rs.getString("name");
			int num = rs.getInt("num");
			System.out.println(name + num);
		}
	}

	public Connection connection(String con, String user, String pw) throws SQLException {
		Connection conn = null;
		try {
			Class.forName("org.apache.hadoop.hive.jdbc.HiveDriver"); // Hive
																		// 单例模式
			conn = DriverManager.getConnection(con, user, pw);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return conn;
	}
}
