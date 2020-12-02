package kr.ac.sejong.data_analysis.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import kr.ac.sejong.data_analysis.graph.Edge;
import kr.ac.sejong.data_analysis.graph.Graph;
import kr.ac.sejong.data_analysis.graph.Vertex;

public class oneGraph implements Graph{

	private Connection connection = null;
	private Statement stmt = null;
	
	public oneGraph() throws SQLException {
		
		connection = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306", "root", "1234");
		stmt = connection.createStatement();
		
		stmt.executeUpdate("CREATE OR REPLACE DATABASE dbp");
		stmt.executeUpdate("USE dbp");
		stmt.executeUpdate("CREATE OR REPLACE TABLE vertex (ID INTEGER PRIMARY KEY, properties JSON)");
		stmt.executeUpdate("CREATE OR REPLACE TABLE edge (OutVertex INTEGER, InVertex INTEGER, label VARCHAR(50), properties JSON)");
	}
	
	public Connection getConnection() {
		return this.connection;
	}
	
	
	@Override
	public Vertex addVertex(String id) {
		try {
			stmt.executeUpdate("INSERT INTO vertex (id) VALUES ("+ id +")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new oneVertex(id, this);
	}

	
	@Override
	public Vertex getVertex(String id) {		
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM vertex WHERE id=" + id + "");
			if (rs.next() == false)
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new oneVertex(id, this);
	}
	

	@Override
	public Iterable<Vertex> getVertices() {
		
		ResultSet rs = null;
		List<Vertex> l = new ArrayList<>();
		try {
			rs = stmt.executeQuery("SELECT * FROM vertex");
			
			while (rs.next()) {
				String id = rs.getString(1);
				l.add(new oneVertex(id, this));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return l;
	}


	@Override
	public Edge addEdge(Vertex outVertex, Vertex inVertex, String label) {
		try {
			ResultSet re = stmt.executeQuery("SELECT * FROM edge WHERE OutVertex = "+outVertex.getId()+" && "
					+ "InVertex = "+inVertex.getId()+" && label = '"+label+"'");
	
			if (re.next() == false) {
				stmt.executeUpdate("INSERT INTO edge (OutVertex, InVertex, label) VALUES ("+outVertex.getId()+", "+inVertex.getId()+", '"+label+"')");
			}
			else 
				return null;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return new oneEdge(outVertex, label, inVertex, this);
	}

	@Override
	public Edge getEdge(Vertex outVertex, Vertex inVertex, String label) {
		try {
			ResultSet re = stmt.executeQuery("SELECT * FROM edge WHERE OutVertex = "+outVertex.getId()+" && "
					+ "InVertex = "+inVertex.getId()+" && label = "+label+"");
			if (re.next() == false)
				return null;
		} catch (SQLException e) {
			e.printStackTrace(); // not exist
		}
		return new oneEdge(outVertex, label, inVertex, this);
	}
	

	@Override
	public Iterable<Edge> getEdges() {
		List<Edge> l = new ArrayList<>();
		try {
			ResultSet re = stmt.executeQuery("SELECT * FROM edge");
			while (re.next()) {
				String out = re.getString(1);
				String in = re.getString(2);
				String label = re.getString(3);
				
				Edge e = new oneEdge(new oneVertex(out, this), label, new oneVertex(in, this), this);
				l.add(e);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return l;
	}
	

	@Override
	public Iterable<Edge> getEdges(String key, Object value) { //JSon
		List<Edge> l = new ArrayList<>();
    	JSONObject job = null;
    	
    	try {
			ResultSet re = stmt.executeQuery("SELECT * FROM edge");
			
			while (re.next()) {
				try {
					job = new JSONObject(re.getString(4));
					Iterator<String> keys = job.keys();
					while (keys.hasNext()) {
						String k = keys.next();
						if (k.equals(key) && job.get(k).equals(value)) {
							l.add(new oneEdge(new oneVertex(re.getString(1), this), re.getString(3), new oneVertex(re.getString(2), this), this));
						}
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return l;
	}
	
	@Override
	public Iterable<Vertex> getVertices(String key, Object value) { //JSon
		List<Vertex> l = new ArrayList<>();
    	JSONObject job = null;
    	
    	try {
			ResultSet re = stmt.executeQuery("SELECT * FROM vertex");
			
			while (re.next()) {
				try {
					job = new JSONObject(re.getString(2));
					Iterator<String> keys = job.keys();
					while (keys.hasNext()) {
						if (job.get(keys.next()).equals(value)) {
							l.add(new oneVertex(re.getString(1), this));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return l;
	}

}
