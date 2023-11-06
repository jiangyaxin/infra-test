package com.jyx.infra.env;

/**
 * @author Archforce
 * @since 2023/11/6 9:23
 */
public class Systems {

    interface Constants {
        String OS_NAME = "os.name";

        String WINDOWS = "WINDOWS";
    }

    public static boolean isWindows() {
        return System.getProperties().getProperty(Constants.OS_NAME).toUpperCase().indexOf(Constants.WINDOWS) != -1;
    }
}
