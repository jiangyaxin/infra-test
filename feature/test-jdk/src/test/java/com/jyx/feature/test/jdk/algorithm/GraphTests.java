package com.jyx.feature.test.jdk.algorithm;

import com.jyx.feature.test.jdk.algorithm.graph.UndirectedGraph;
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
        UndirectedGraph undirectedGraph = new UndirectedGraph();
        undirectedGraph.addEdge(0,1);
        undirectedGraph.addEdge(0,2);
        undirectedGraph.addEdge(0,6);
        undirectedGraph.addEdge(0,5);


        undirectedGraph.addEdge(3,4);
        undirectedGraph.addEdge(3,5);

        undirectedGraph.addEdge(4,5);
        undirectedGraph.addEdge(4,6);

        log.info(undirectedGraph.print());
    }
}
