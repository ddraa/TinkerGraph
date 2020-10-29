 package kr.ac.sejong.data_analysis.solution;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		
		Connection connection = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306", "root", "1234");
		
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("CREATE OR REPLACE DATABASE DBPS");
		stmt.executeUpdate("USE DBPS");
		stmt.executeUpdate("CREATE OR REPLACE TABLE customer (NAME VARCHAR(50), CUSTOMER_ID INTEGER, LAST_VISIT DATE)");
		
		stmt.executeUpdate("INSERT INTO customer VALUES ('Jane Markham', 1, '2020-03-15')");
		stmt.executeUpdate("INSERT INTO customer VALUES ('Louis Smith', 2, '2020-04-10')");
		stmt.executeUpdate("INSERT INTO customer VALUES ('Woodrow Lang', 3, '2020-05-25')");
		stmt.executeUpdate("INSERT INTO customer VALUES ('Dr.Johnson', 4, '2020-06-21')");

		
		ResultSet rs = stmt.executeQuery("SELECT * FROM customer");
		while(rs.next()) {
			String name = rs.getString(1);
			int id = rs.getInt(2);
			Date lv = rs.getDate(3);
			
			System.out.println(name + " " + id + " " + lv);
		}
		
		stmt.close();
		connection.close();
		
	}

}
