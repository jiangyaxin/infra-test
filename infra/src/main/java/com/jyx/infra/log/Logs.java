package com.jyx.infra.log;

import org.slf4j.Logger;

/**
 * @author Archforce
 * @since 2023/2/27 13:03
 */
public class Logs {

    public static void error(Logger log, String msg, Object... arguments) {
        if (log.isErrorEnabled()) {
            log.error(msg, arguments);
        }
    }

    public static void warn(Logger log, String msg, Object... arguments) {
        if (log.isWarnEnabled()) {
            log.warn(msg, arguments);
        }
    }

    public static void info(Logger log, String msg, Object... arguments) {
        if (log.isInfoEnabled()) {
            log.info(msg, arguments);
        }
    }

    public static void debug(Logger log, String msg, Object... arguments) {
        if (log.isDebugEnabled()) {
            log.debug(msg, arguments);
        }
    }
}
