package com.jyx.infra.mybatis.plus.jdbc.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.lang.Nullable;

import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

/**
 * @author Archforce
 * @since 2023/11/23 13:01
 */
@Slf4j
public class JdbcHelper {

    public static void handleWarnings(Statement stmt, boolean ignoreWarnings) throws SQLException {
        if (ignoreWarnings) {
            if (log.isDebugEnabled()) {
                SQLWarning warningToLog = stmt.getWarnings();
                while (warningToLog != null) {
                    log.debug("SQLWarning ignored: SQL state '" + warningToLog.getSQLState() + "', error code '" +
                            warningToLog.getErrorCode() + "', message [" + warningToLog.getMessage() + "]");
                    warningToLog = warningToLog.getNextWarning();
                }
            }
        } else {
            handleWarnings(stmt.getWarnings());
        }
    }


    public static void handleWarnings(@Nullable SQLWarning warning) throws SQLWarningException {
        if (warning != null) {
            throw new SQLWarningException("Warning not ignored", warning);
        }
    }

}
