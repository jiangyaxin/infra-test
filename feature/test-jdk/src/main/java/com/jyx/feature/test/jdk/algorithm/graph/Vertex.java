package com.jyx.feature.test.jdk.algorithm.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Archforce
 * @since 2023/7/7 15:15
 */
public class Vertex {

    /**
     * 顶点ID
     */
    private final Integer id;


    /**
     * 入度
     */
    private final Set<Integer> in = new HashSet<>();

    /**
     * 出度
     */
    private final Set<Integer> out = new HashSet<>();

    public Vertex(Integer id) {
        this.id = id;
    }

    public void addEdge(Integer w) {
        in.add(w);
        out.add(w);
    }

    public Integer getId() {
        return id;
    }


    public Set<Integer> getIn() {
        return in;
    }

    public Set<Integer> getOut() {
        return out;
    }
}
