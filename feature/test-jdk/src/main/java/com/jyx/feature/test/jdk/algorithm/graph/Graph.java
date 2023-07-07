package com.jyx.feature.test.jdk.algorithm.graph;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Archforce
 * @since 2023/7/7 15:13
 */
public class Graph {

    /**
     * 顶点的数量
     */
    private int vertexNum = 0;

    /**
     * 边的数量
     */
    private int edgeNum = 0;

    /**
     * 领接表 AdjacencyList
     */
    private final Map<Integer,Vertex> adj = new HashMap<>();


    public void addEdge(Integer v,Integer w) {
        connect(v, w);
        connect(w, v);
        edgeNum++;
    }

    private void connect(Integer out, Integer in) {
        if (adj.containsKey(in)) {
            Vertex vertexW = adj.get(in);
            vertexW.addEdge(out);
        } else {
            vertexNum++;
            Vertex vertexW = new Vertex(in);
            vertexW.addEdge(out);
            adj.put(in,vertexW);
        }
    }

    public String print() {
        StringBuilder sb = new StringBuilder("\r\n");
        for(Vertex vertex : adj.values()) {
            sb.append(String.format("Node %s out connected node %s \r\n",vertex.getId(),vertex.getOut()));
        }
        return sb.toString();
    }

}
