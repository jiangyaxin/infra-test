package com.jyx.infra.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * @author Archforce
 * @since 2023/10/30 17:27
 */
public class DateTimeUtil {

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
