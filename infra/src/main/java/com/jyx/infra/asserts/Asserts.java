package com.jyx.infra.asserts;

import com.jyx.infra.exception.AppException;

import java.util.function.Supplier;

/**
 * @author jiangyaxin
 * @since 2023/3/1 18:29
 */
public class Asserts {

    public static void isTrue(boolean expression, Supplier<String> message) {
        if (!expression) {
            throw AppException.of(message.get());
        }
    }

    public static void hasText(String text, Supplier<String> message) {
        if (!hasText(text)) {
            throw AppException.of(message.get());
        }
    }

    public static void notNull(Object object, Supplier<String> message) {
        if (object == null) {
            throw new IllegalArgumentException(message.get());
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
