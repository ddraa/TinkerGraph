package kr.ac.sejong.data_analysis.test;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
       TinkerGraph g = new TinkerGraph();
       
       g.addVertex("0");
       Vertex v = g.getVertex("0");
       
       System.out.println(v);
    }
}
