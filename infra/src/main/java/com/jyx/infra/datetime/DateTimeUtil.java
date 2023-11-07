package com.jyx.infra.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * @author Archforce
 * @since 2023/10/30 17:27
 */
public class DateTimeUtil {

    public static LocalDate toLocalDate(Date date) {
        return LocalDate.ofInstant(date.toInstant(), DateTimeConstant.ZoneOffsets.DEFAULT_ZONE);
    }

    public static String formatDate(Date date, DateTimeFormatter dateTimeFormatter) {
        LocalDate localDate = LocalDate.ofInstant(date.toInstant(), DateTimeConstant.ZoneOffsets.DEFAULT_ZONE);
        String dateStr = localDate.format(dateTimeFormatter);

        return dateStr;
    }

    public static Date parseDate(String dateStr, DateTimeFormatter dateTimeFormatter) {
        LocalDate localDate = LocalDate.parse(dateStr, dateTimeFormatter);
        return Date.from(localDate.atStartOfDay().atZone(DateTimeConstant.ZoneOffsets.DEFAULT_ZONE).toInstant());
    }

    public static boolean checkDateTime(String dateTime, DateTimeFormatter dateTimeFormatter) {
        try {
            LocalDateTime.parse(dateTime, dateTimeFormatter);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    public static boolean checkDate(String date, DateTimeFormatter dateTimeFormatter) {
        try {
            LocalDate.parse(date, dateTimeFormatter);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }
}
