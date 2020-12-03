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
    
    public oneVertex(String id, oneGraph graph) {
    	this.id = id;
    	this.graph = graph;
    }
    

    @Override
    public Iterable<Edge> getEdges(Direction direction, String... labels) {
    	List<Edge> li = new ArrayList<>();
    	
		Connection connection = this.graph.getConnection();
		Statement stmt = null;
		
		try {
			stmt = connection.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		}
		
    	if (direction==Direction.IN) {
    		try {
				ResultSet rs = stmt.executeQuery("SELECT * FROM edge WHERE InVertex = "+this.id+"");
				while (rs.next()) {
					for (String s : labels) {
						if (s.equals(rs.getString(3))) {
							li.add(new oneEdge(new oneVertex(rs.getString(1), this.graph), s, new oneVertex(rs.getString(2), this.graph), this.graph));
							break;
						}
					}
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}    		
    	}
    	else {
    		try {
				ResultSet rs = stmt.executeQuery("SELECT * FROM edge WHERE OutVertex = "+this.id+"");
				while (rs.next()) {
					for (String s : labels) {
						if (s.equals(rs.getString(3))) {
							li.add(new oneEdge(new oneVertex(rs.getString(1), this.graph), s, new oneVertex(rs.getString(2), this.graph), this.graph));
							break;
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
        return li;
    }

    
    @Override 
    public Iterable<Vertex> getVertices(Direction direction, String... labels) {
    	List<Vertex> li = new ArrayList<>();
    	
		Connection connection = this.graph.getConnection();
		Statement stmt = null;
		
		try {
			stmt = connection.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		}
		
    	if (direction==Direction.IN) {
    		try {
				ResultSet rs = stmt.executeQuery("SELECT * FROM edge WHERE InVertex = "+this.id+"");
				while (rs.next()) {
					for (String s : labels) {
						if (s.equals(rs.getString(3))) {
							li.add(new oneVertex(rs.getString(1), this.graph));
							break;
						}
					}
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}    		
    	}
    	else {
    		try {
				ResultSet rs = stmt.executeQuery("SELECT * FROM edge WHERE OutVertex = "+this.id+"");
				while (rs.next()) {
					for (String s : labels) {
						if (s.equals(rs.getString(3))) {
							li.add(new oneVertex(rs.getString(2), this.graph));
							break;
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
        return li;
    }
    

    @Override
    public Edge addEdge(String label, Vertex inVertex) {
		Connection connection = this.graph.getConnection();
		Statement stmt = null;
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
    	Statement stmt = null;
    	JSONObject job = null;
    	Connection connection = this.graph.getConnection();
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
    	Statement stmt = null;
    	JSONObject job = null;
    	Connection connection = this.graph.getConnection();
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
    	Statement stmt = null;
    	JSONObject job = null;
    	Connection connection = this.graph.getConnection();
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