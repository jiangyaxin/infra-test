package com.jyx.feature.test.arthas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author JYX
 */
@ComponentScan("com.jyx")
@SpringBootApplication
public class ArthasApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArthasApplication.class, args);
    }

}
