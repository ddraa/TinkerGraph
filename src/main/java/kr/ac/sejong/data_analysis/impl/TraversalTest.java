package kr.ac.sejong.data_analysis.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;

import kr.ac.sejong.data_analysis.graph.Direction;
import kr.ac.sejong.data_analysis.graph.Graph;
import kr.ac.sejong.data_analysis.graph.Vertex;

public class TraversalTest { // Team 1

	public static void createGraph(Graph g) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("/Users/dragon/rdata.txt"));
		int cnt = 0;
		while (true) {
			String line = br.readLine();
			if (line == null)
				break;
			if (line.startsWith("#"))
				continue;
			if (cnt == 10000)
				return;
			
			String[] arr = line.split("\t");

			Vertex vl = g.getVertex(arr[0]);
			if (vl == null) {
				vl = g.addVertex(arr[0]);
			}
			Vertex vr = g.getVertex(arr[1]);
			if (vr == null) {
				vr = g.addVertex(arr[1]);
			}

			g.addEdge(vl, vr, "label");
			System.out.println(cnt++);
		}
		br.close();
	}

	
	public static void getReachableVertices(Graph g) throws IOException {
		BufferedWriter w = new BufferedWriter(new FileWriter("/Users/dragon/rv.txt"));
		Iterator<Vertex> iter = g.getVertices().iterator();
		while (iter.hasNext()) {
			Vertex v = iter.next();
            HashSet<String> rv = new HashSet<String>();	
					
			Iterator<Vertex> vi = v.getVertices(Direction.OUT, "label").iterator(); // each vertex
			while(vi.hasNext()) {
				Vertex ov = vi.next();
				rv.add(ov.getId().toString());
				Iterator<Vertex> vi2 = ov.getVertices(Direction.OUT, "label").iterator();
				while(vi2.hasNext()) {
					Vertex ov2 = vi2.next();
					rv.add(ov2.getId().toString());
				}
			}
                                   
			System.out.println(v.toString() + " : " + rv.size());
			w.write(v.toString() + " : " + rv.size() + "\n");
		}
		w.close();
	}

	public static void main(String[] args) throws IOException, SQLException {
		Graph g = new oneGraph();

		long p = System.currentTimeMillis();
		createGraph(g);
		System.out.println("Graph Creation (ms.): " + (System.currentTimeMillis() - p));

		p = System.currentTimeMillis();
		getReachableVertices(g);
		System.out.println("RV (ms.): " + (System.currentTimeMillis() - p));
		//RV (ms.): 93792 
		//RV : 249081
		//RV : 247855
	}
}
