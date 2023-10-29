package com.jyx.infra.datetime;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author JYX
 * @since 2021/10/19 17:37
 */
public interface DateTimeConstant {

    interface Patterns {
        String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    }

    interface ZoneOffsets {
        ZoneOffset DEFAULT_ZONE = ZoneOffset.ofHours(8);
    }

    interface DateTimeFormatters {
        DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(Patterns.DEFAULT_DATETIME_PATTERN);
    }
}
