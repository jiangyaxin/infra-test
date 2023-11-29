package com.jyx.infra.util;

import java.io.File;

/**
 * @author jiangyaxin
 * @since 2023/11/6 9:23
 */
public class SystemUtil {

    interface Constants {
        String OS_NAME = "os.name";

        String WINDOWS = "WINDOWS";

        String JAVA_IO_TMPDIR = "java.io.tmpdir";
    }

    public static String getJavaIoTmpDir() {
        String javaIoTmpDir = System.getProperties().getProperty(Constants.JAVA_IO_TMPDIR);
        if (!javaIoTmpDir.endsWith(File.separator)) {
            javaIoTmpDir += File.separator;
        }
        return javaIoTmpDir;
    }

    public static boolean isWindows() {
        return System.getProperties().getProperty(Constants.OS_NAME).toUpperCase().indexOf(Constants.WINDOWS) != -1;
    }
}
