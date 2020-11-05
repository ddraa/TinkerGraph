package kr.ac.sejong.data_analysis.solution;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.sql.Statement;

public class EmaillApp {

	
	public static void main(String[] args) throws IOException, SQLException {
		
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306", "root", "1234");
		}catch(SQLInvalidAuthorizationSpecException e){
			e.printStackTrace();
			System.out.println("PASSWORD ERROR");
			System.exit(1);
		}
		
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("CREATE OR REPLACE DATABASE dbp");
		stmt.executeUpdate("USE dbp");
		stmt.executeUpdate("CREATE OR REPLACE TABLE email (source INTEGER, destination INTEGER)");


		
		
		BufferedReader r = new BufferedReader(
				new FileReader("/Users/dragon/data.txt"));
		
		while(true) {
			String line = r.readLine();
			if (line == null){
				break;
			}
			if (line.startsWith("#")) {
				continue;
			}
			
			String[] arr = line.split("\t");
			int left = Integer.parseInt(arr[0]);
			int right = Integer.parseInt(arr[1]);
			
			stmt.executeUpdate("INSERT INTO email VALUES("+ left +", "+ right +")");

			System.out.println(left + " -> " + right);
		}
		r.close();
	}

}
