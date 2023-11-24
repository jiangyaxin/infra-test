package com.jyx.feature.test.jdk.algorithm.graph;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jiangyaxin
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
    private final Map<Integer, Vertex> adj = new HashMap<>();

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
        Vertex vertex = adj.get(v);
        if (vertex == null) {
            throw new RuntimeException(String.format("节点 %s 找不到", v));
        }
        return vertex.getDegree();
    }

    /**
     * 计算最大度
     */
    public int maxDegree() {
        int maxDegree = adj.values().stream()
                .mapToInt(Vertex::getDegree)
                .max()
                .getAsInt();
        return maxDegree;
    }

    /**
     * 计算平均度
     */
    public int avgDegree() {
        int sum = adj.values().stream()
                .mapToInt(Vertex::getDegree)
                .sum();
        return sum / vertexNum;
    }

    /**
     * 自环的个数
     */
    public int numOfSelfLoop() {
        int count = 0;
        for (Vertex vertex : adj.values()) {
            for (Integer near : vertex.getBag()) {
                if (near == vertex.getId()) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    private void connect(Integer out, Integer in) {
        if (adj.containsKey(in)) {
            Vertex vertexW = adj.get(in);
            vertexW.addEdge(out);
        } else {
            vertexNum++;
            Vertex vertexW = new Vertex(in);
            vertexW.addEdge(out);
            adj.put(in, vertexW);
        }
    }

    public String print() {
        StringBuilder sb = new StringBuilder("\n");
        sb.append(String.format("Graph has %s vertex，%s edge\n", vertexNum, edgeNum));
        for (Vertex vertex : adj.values()) {
            sb.append(String.format("Node %s out connected node %s \n", vertex.getId(), vertex.getBag()));
        }
        return sb.toString();
    }

}
