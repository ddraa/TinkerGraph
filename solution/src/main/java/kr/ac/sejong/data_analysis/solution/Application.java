 package kr.ac.sejong.data_analysis.solution;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.sql.Statement;

public class Application {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306", "root", "1234");
		}catch(SQLInvalidAuthorizationSpecException e) {
			e.printStackTrace();
			System.out.println("PASSWORD ERROR");
			System.exit(1);
		}
		
		Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		stmt.executeUpdate("CREATE OR REPLACE DATABASE DBPS");
		stmt.executeUpdate("USE DBPS");
		stmt.executeUpdate("CREATE OR REPLACE TABLE customer (NAME VARCHAR(50), CUSTOMER_ID INTEGER PRIMARY KEY, LAST_VISIT DATE)");
		
		stmt.executeUpdate("INSERT INTO customer VALUES ('Jane Markham', 1, '2020-03-15')");
		stmt.executeUpdate("INSERT INTO customer VALUES ('Louis Smith', 2, '2020-04-10')");
		stmt.executeUpdate("INSERT INTO customer VALUES ('Woodrow Lang', 3, '2020-05-25')");
		stmt.executeUpdate("INSERT INTO customer VALUES ('Dr.Johnson', 4, '2020-06-21')");

		
		ResultSet rs = stmt.executeQuery("SELECT * FROM customer");
		while(rs.next()) {
			String name = rs.getString(1);
			
			//int id = rs.getInt(2);
			//Object id = rs.getObject(2);
			Integer id = (Integer) rs.getObject(2); // type transfer
			
			Date lv = rs.getDate(3);
			
			System.out.println(name + " " + id + " " + lv);
		}
	
		rs.moveToInsertRow(); // place the cursor
		rs.updateString(1, "Glan");
		rs.updateInt(2, 35244);
		rs.insertRow();
		
		
		stmt.close();
		connection.close();
		
	}

}
