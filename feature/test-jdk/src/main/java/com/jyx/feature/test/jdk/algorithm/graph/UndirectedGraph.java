package com.jyx.feature.test.jdk.algorithm.graph;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Archforce
 * @since 2023/7/7 15:13
 */
public class UndirectedGraph {

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
    private final Map<Integer, UndirectedVertex> adj = new HashMap<>();

    /**
     * 新增边
     */
    public void addEdge(Integer v, Integer w) {
        connect(v, w);
        connect(w, v);
        edgeNum++;
    }

    /**
     * 计算V的度
     */
    public int degree(Integer v) {
        UndirectedVertex undirectedVertex = adj.get(v);
        if (undirectedVertex == null) {
            throw new RuntimeException(String.format("节点 %s 找不到", v));
        }
        return undirectedVertex.getDegree();
    }

    /**
     * 计算最大度
     */
    public int maxDegree() {
        int maxDegree = adj.values().stream()
                .mapToInt(UndirectedVertex::getDegree)
                .max()
                .getAsInt();
        return maxDegree;
    }

    /**
     * 计算平均度
     */
    public int avgDegree() {
        int sum = adj.values().stream()
                .mapToInt(UndirectedVertex::getDegree)
                .sum();
        return sum / vertexNum;
    }

    /**
     * 自环的个数
     */
    public int numOfSelfLoop() {
        int count = 0;
        for (UndirectedVertex undirectedVertex : adj.values()) {
            for (Integer near : undirectedVertex.getBag()) {
                if (near == undirectedVertex.getId()) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    private void connect(Integer out, Integer in) {
        if (adj.containsKey(in)) {
            UndirectedVertex undirectedVertexW = adj.get(in);
            undirectedVertexW.addEdge(out);
        } else {
            vertexNum++;
            UndirectedVertex undirectedVertexW = new UndirectedVertex(in);
            undirectedVertexW.addEdge(out);
            adj.put(in, undirectedVertexW);
        }
    }

    public String print() {
        StringBuilder sb = new StringBuilder("\n");
        sb.append(String.format("Graph has %s vertex，%s edge\n", vertexNum, edgeNum));
        for (UndirectedVertex undirectedVertex : adj.values()) {
            sb.append(String.format("Node %s out connected node %s \n", undirectedVertex.getId(), undirectedVertex.getBag()));
        }
        return sb.toString();
    }

}
