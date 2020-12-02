package kr.ac.sejong.data_analysis.solv;

import java.util.Set;



public class JVertex implements Vertex {

    //예: string 형태의 고유 아이디, '|' 사용 금지
    private String id;

    @Override
    public Iterable<Edge> getEdges(Direction direction, String... labels) {
        return null;
    }

    @Override
    public Iterable<Vertex> getVertices(Direction direction, String... labels) {
        return null;
    }

    @Override
    public Edge addEdge(String label, Vertex inVertex) {
        return null;
    }

    @Override
    public Object getProperty(String key) {
        return null;
    }

    @Override
    public Set<String> getPropertyKeys() {
        return null;
    }

    @Override
    public void setProperty(String key, Object value) {

    }

    @Override
    public Object getId() {
        return null;
    }
}