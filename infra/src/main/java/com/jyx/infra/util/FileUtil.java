package com.jyx.infra.util;

import com.jyx.infra.log.Logs;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author Archforce
 * @since 2023/11/29 15:29
 */
@Slf4j
public class FileUtil {

    public static void recursiveDelete(Path path) {
        try {
            Files.walkFileTree(path,
                    new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult visitFile(Path file,
                                                         BasicFileAttributes attrs) throws IOException {
                            Files.delete(file);
                            Logs.debug(log, "Deleted file : {}", file);
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult postVisitDirectory(Path dir,
                                                                  IOException exc) throws IOException {
                            Files.delete(dir);
                            Logs.debug(log, "Deleted directory : {}", dir);
                            return FileVisitResult.CONTINUE;
                        }

                    }
            );
            Logs.info(log, "Recursive delete: {}", path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
