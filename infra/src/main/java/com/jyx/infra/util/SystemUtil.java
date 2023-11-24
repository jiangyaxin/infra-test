package com.jyx.infra.util;

/**
 * @author jiangyaxin
 * @since 2023/11/6 9:23
 */
public class SystemUtil {

    interface Constants {
        String OS_NAME = "os.name";

        String WINDOWS = "WINDOWS";
    }

    public static boolean isWindows() {
        return System.getProperties().getProperty(Constants.OS_NAME).toUpperCase().indexOf(Constants.WINDOWS) != -1;
    }
}
