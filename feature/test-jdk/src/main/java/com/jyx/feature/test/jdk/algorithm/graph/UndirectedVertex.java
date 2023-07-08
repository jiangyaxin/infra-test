package com.jyx.feature.test.jdk.algorithm.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Archforce
 * @since 2023/7/7 15:15
 */
public class UndirectedVertex {

    /**
     * 顶点ID
     */
    private final Integer id;


    /**
     * 入度
     */
    private final Set<Integer> bag = new HashSet<>();


    public UndirectedVertex(Integer id) {
        this.id = id;
    }

    public void addEdge(Integer w) {
        bag.add(w);
    }

    public int getDegree() {
        return bag.size();
    }

    public Integer getId() {
        return id;
    }


    public Set<Integer> getBag() {
        return bag;
    }
}
