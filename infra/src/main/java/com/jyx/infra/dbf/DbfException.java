package com.jyx.infra.dbf;

/**
 * @author Archforce
 * @since 2023/11/6 10:24
 */
public class DbfException extends RuntimeException {

    public static DbfException of(String message) {
        return new DbfException(message);
    }

    public static DbfException of(String message, Throwable cause) {
        return new DbfException(message, cause);
    }

    public static DbfException of(Throwable cause) {
        return new DbfException(cause);
    }

    public DbfException() {
    }

    public DbfException(String message) {
        super(message);
    }

    public DbfException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbfException(Throwable cause) {
        super(cause);
    }

    public DbfException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
