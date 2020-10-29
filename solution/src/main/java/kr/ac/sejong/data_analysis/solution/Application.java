 package kr.ac.sejong.data_analysis.solution;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		
		Connection connection = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306", "root", "1234");
		
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("CREATE OR REPLACE DATABASE DBPS");
		stmt.executeUpdate("USE DBPS");
		stmt.executeUpdate("CREATE OR REPLACE TABLE 사원 (사원번호 INTEGER, 직속상사번호 INTEGER, 이름 VARCHAR(50), 월급 DOUBLE,"
				+ "직급 VARCHAR(50)) CHARACTER SET 'euckr';");
		stmt.executeUpdate("INSERT INTO 사원 VALUES (1, null, '송경용', 50000, '사장')");
		
		stmt.close();
		connection.close();
		
	}

}
