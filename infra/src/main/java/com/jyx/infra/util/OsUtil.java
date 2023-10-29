package com.jyx.infra.util;

/**
 * @author Archforce
 * @since 2023/10/29 17:56
 */
public class OsUtil {

    public static boolean isWindows() {
        return System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
    }
}
