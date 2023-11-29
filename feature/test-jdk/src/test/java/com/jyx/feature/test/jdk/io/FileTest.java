package com.jyx.feature.test.jdk.io;

import com.jyx.infra.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

/**
 * @author jiangyaxin
 * @since 2023/8/25 16:43
 */
@Slf4j
public class FileTest {

    @Test
    public void existTest() {
        String filePath = "D:\\1\\1.txt";
        File file = new File(filePath);
        File parentFile = file.getParentFile();
        log.info("1");
    }

    @Test
    public void deleteTest() {
        FileUtil.delete(Paths.get("D:\\application\\spring-test-experience\\infra\\target"));
    }
}
