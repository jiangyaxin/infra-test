package com.jyx.infra.asserts;

import com.jyx.infra.exception.AppException;

/**
 * @author Archforce
 * @since 2023/3/1 18:29
 */
public class Asserts {

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw AppException.of(message);
        }
    }

    public static void hasText(String text, String message) {
        if (!hasText(text)) {
            throw AppException.of(message);
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private static boolean hasText(String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
