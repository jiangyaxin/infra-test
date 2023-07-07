package com.jyx.feature.test.jdk.algorithm;

import com.jyx.feature.test.jdk.algorithm.graph.Graph;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Archforce
 * @since 2023/7/7 15:33
 */
@Slf4j
public class GraphTests {


    @Test
    public void newGraphTest() {
        Graph graph = new Graph();
        graph.addEdge(0,1);
        graph.addEdge(0,2);
        graph.addEdge(0,6);
        graph.addEdge(0,5);

        graph.addEdge(1,0);
        graph.addEdge(2,0);

        graph.addEdge(3,4);
        graph.addEdge(3,5);

        graph.addEdge(4,3);
        graph.addEdge(4,5);
        graph.addEdge(4,6);

        graph.addEdge(5,0);
        graph.addEdge(5,3);
        graph.addEdge(5,4);

        graph.addEdge(6,0);
        graph.addEdge(6,4);

        log.info(graph.print());
    }
}
