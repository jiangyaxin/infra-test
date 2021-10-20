package com.jyx.experience.datetime;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author JYX
 * @since 2021/10/19 17:37
 */
public interface DateTimeConstant {

    ZoneOffset ZONE_DEFAULT = ZoneOffset.ofHours(8);

    DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
