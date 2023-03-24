package com.jyx.feature.test.flink;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;

/**
 * @author asa
 */
@Slf4j
public class FlinkApplication {

    public static void main(String[] args) {
        ExecutionEnvironment environment = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<String> dataSet = environment.fromElements(
                "1", "2", "3"
        );

    }

}
