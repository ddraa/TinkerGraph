package kr.ac.sejong.data_analysis.solution;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.sql.Statement;

public class Q3 {

	
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
		stmt.executeUpdate("SET max_heap_table_size= 1024*1024*128");
		stmt.executeUpdate("CREATE OR REPLACE TABLE emailmem (source INTEGER, destination INTEGER) ENGINE = MEMORY");
		stmt.executeUpdate("CREATE OR REPLACE INDEX idx ON emailmem (source) USING HASH"); // INDEXING !!

		//PreparedStatement pstmt = connection.prepareStatement("INSERT INTO EMAIL VALUES (?, ?)");
	
		
		// file read
		BufferedReader r = new BufferedReader(new FileReader("/Users/dragon/data.txt"));
		long pre = System.currentTimeMillis();
		int cnt = 0;
		
		while(true) {
			String line = r.readLine();
			if (line == null){
				break;
			}
			if (line.startsWith("#")) {
				continue;
			}
			
			String[] arr = line.split("\t"); // cur type string
			int left = Integer.parseInt(arr[0]);
			int right = Integer.parseInt(arr[1]);
			
			String sql = "INSERT INTO emailmem VALUES("+left+", "+right+")";
			//stmt.executeUpdate(sql); //-> original, 117599ms
			
			
			// 117429ms
			//pstmt.clearParameters();
			//pstmt.setInt(1, left);
			//pstmt.setInt(2, right);
			//pstmt.executeUpdate();
			
			
			stmt.addBatch(sql); // even more faster ? throw the batch ! fastest 93849 !
			
			// in memory -> 10186.... most
		
			System.out.println((System.currentTimeMillis() - pre) + ":" + ++cnt);
			//System.out.println(left + " -> " + right);
		}
		stmt.executeBatch();
		System.out.println((System.currentTimeMillis() - pre) + ":" + ++cnt); // batch check
		
		r.close();
		stmt.close();
		connection.close();
	}

}
