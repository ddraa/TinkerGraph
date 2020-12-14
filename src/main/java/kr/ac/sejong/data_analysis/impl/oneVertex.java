package kr.ac.sejong.data_analysis.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import kr.ac.sejong.data_analysis.graph.Direction;
import kr.ac.sejong.data_analysis.graph.Edge;
import kr.ac.sejong.data_analysis.graph.Graph;
import kr.ac.sejong.data_analysis.graph.Vertex;

public class oneVertex implements Vertex {

    private String id;
    private oneGraph graph;
    Statement stmt = null;
    ResultSet rs = null;
    Connection connection=null;
    
    public oneVertex(String id, oneGraph graph) {
    	this.id = id;
    	this.graph = graph;
    	this.connection = this.graph.getConnection();
    }
    

    @Override
    public Iterable<Edge> getEdges(Direction direction, String... labels) { 
    	List<Edge> li = new ArrayList<>();
    			
		try {
			stmt = connection.createStatement();
			if (direction==Direction.IN) {
    			stmt.executeUpdate("CREATE OR REPLACE INDEX IDX ON edge (InVertex, OutVertex)");
				rs = stmt.executeQuery("SELECT OutVertex FROM edge WHERE InVertex = "+this.id+" && label = "+labels[0]+"");
				while (rs.next()) {
					li.add(new oneEdge(new oneVertex(rs.getString(1), this.graph), labels[0], this, this.graph));
				}	
	    	}
			else {
    			stmt.executeUpdate("CREATE OR REPLACE INDEX IDX ON edge (OutVertex, InVertex)");
				rs = stmt.executeQuery("SELECT InVertex FROM edge WHERE OutVertex = "+this.id+" && label = "+labels[0]+"");
				while (rs.next()) {
					li.add(new oneEdge(this, labels[0], new oneVertex(rs.getString(1), this.graph), this.graph));
				}
	    	}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		}
		
        return li;
    }

    
    @Override 
    public Iterable<Vertex> getVertices(Direction direction, String... labels) { 
    	List<Vertex> li = new ArrayList<>();
    			
		try {
			stmt = connection.createStatement();
			if (direction==Direction.IN) {
				stmt.executeUpdate("CREATE OR REPLACE INDEX IDX ON edge (InVertex, OutVertex)");
				rs = stmt.executeQuery("SELECT OutVertex FROM edge WHERE InVertex = "+this.id+" && label = "+labels[0]+"");
			}
			else {
				stmt.executeUpdate("CREATE OR REPLACE INDEX IDX ON edge (OutVertex, InVertex)");
				rs = stmt.executeQuery("SELECT InVertex FROM edge WHERE OutVertex = "+this.id+" && label = "+labels[0]+"");
			}
			while (rs.next()) {
				li.add(new oneVertex(rs.getString(1), this.graph));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		}
        return li;
    }
    

    @Override
    public Edge addEdge(String label, Vertex inVertex) { 
		Edge ed = null;
		
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate("INSERT INTO edge (OutVertex, InVertex, label) VALUES ("+this.id+", "+inVertex.getId()+", '"+label+"')");
			ed = new oneEdge(this, label, inVertex, this.graph);
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return ed;
    }

    
    @Override
    public Object getProperty(String key) { 
    	JSONObject job = null;
    	try {
			stmt = connection.createStatement();
			ResultSet re = stmt.executeQuery("SELECT properties from vertex WHERE ID = "+this.id+" AND Properties IS NOT NULL");
			if (re.next() == false) {
				return null;
			}
			else {
				try {
					job = new JSONObject(re.getString(1));
					return job.get(key);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
        return null;
    }

    @Override
    public Set<String> getPropertyKeys() { 
    	Set<String> s = new HashSet<String>();
    	JSONObject job = null;
    	try {
			stmt = connection.createStatement();
			ResultSet re = stmt.executeQuery("SELECT properties from vertex WHERE ID = "+this.id+" AND Properties IS NOT NULL");
			
			if (re.next() == false) {
				return null; 
			}
			else {
				try {
					job = new JSONObject(re.getString(1));
					Iterator<String> it = job.keys();
					while(it.hasNext()) {
						s.add(it.next());
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
        return s;
    }

    @Override
    public void setProperty(String key, Object value) {  
    	JSONObject job = null;
    	
		try {
			stmt = connection.createStatement();
			ResultSet re = stmt.executeQuery("SELECT properties from vertex WHERE ID = "+this.id+" AND properties IS NOT NULL");
			
			if (re.next() == false) {
				job = new JSONObject();
			}
			else {
				try {
					job = new JSONObject(re.getString(1));
					job.remove(key);
				} catch (JSONException e) {
					e.printStackTrace();
				}	
			}
			try {
				job.put(key, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			stmt.executeUpdate("UPDATE vertex SET properties = \'"+job.toString()+"\' WHERE ID = "+this.id+"");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    

    @Override
	public String toString() { 
		return "v["+this.id+"]";
	}


	@Override
    public Object getId() {
        return this.id;
    }
}