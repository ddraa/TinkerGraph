package kr.ac.sejong.data_analysis.solution;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

public class Q2 {
	public static void main(String[] args) throws IOException, SQLException {

		Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306", "root", "1234");
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS dbp");
		stmt.executeUpdate("USE dbp");

		// Step1
		HashSet<Integer> idSet = new HashSet<Integer>();
		ResultSet set = stmt.executeQuery("SELECT DISTINCT source from email");
		while (set.next()) {
			idSet.add(set.getInt(1));
		}
		set = stmt.executeQuery("SELECT DISTINCT destination from email");
		while (set.next()) {
			idSet.add(set.getInt(1));
		}

		System.out.println(idSet.size());

		// Step2
		int cnt = 0;
		long pre = System.currentTimeMillis();
		for (Integer id : idSet) {
			if (cnt++ == 100)
				break;
			HashSet<Integer> receiverSet = new HashSet<Integer>();
			ResultSet rset = stmt.executeQuery("SELECT DISTINCT destination FROM email WHERE source = " + id);
			while (rset.next()) {
				receiverSet.add(rset.getInt(1));
			}
			HashSet<Integer> receiverSet2 = new HashSet<Integer>();
			for (Integer r : receiverSet) {
				ResultSet rset2 = stmt.executeQuery("SELECT DISTINCT destination FROM email WHERE source = " + r);
				while (rset2.next()) {
					receiverSet2.add(rset2.getInt(1));
				}
			}
		System.out.println(id + " -> " + receiverSet2);
	}
	System.out.println("ET: " + (System.currentTimeMillis() - pre));
	// ET: 23263
	stmt.close();
	connection.close();
}
}


