package com.jyx.infra.spring.jdbc.mysql;

import static com.jyx.infra.spring.jdbc.KeyWorkConstant.*;
import static com.jyx.infra.spring.jdbc.KeyWorkConstant.EMPTY;

/**
 * @author Archforce
 * @since 2023/11/25 12:31
 */
public class SqlHelper {

    public static String formatWhere(String where) {
        if (where == null || where.trim().isEmpty()) {
            return "";
        }
        String tmpWhere = where.trim().toUpperCase();
        if (tmpWhere.startsWith(WHERE)) {
            return where;
        } else {
            return WHERE + EMPTY + where;
        }
    }

    public static String formatSelect(String select) {
        if (select == null || select.trim().isEmpty()) {
            return EMPTY + ALL_SELECT + EMPTY;
        }
        return EMPTY + select + EMPTY;
    }

    public static String formatKeyWork(String str) {
        String trim = str.trim();
        StringBuilder result = new StringBuilder();
        if (!trim.startsWith(BACKTICK)) {
            result.append(BACKTICK);
        }
        result.append(trim);
        if (!trim.endsWith(BACKTICK)) {
            result.append(BACKTICK);
        }
        return result.toString();
    }
}
