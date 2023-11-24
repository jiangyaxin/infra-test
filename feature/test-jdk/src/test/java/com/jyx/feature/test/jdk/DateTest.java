package com.jyx.feature.test.jdk;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author jiangyaxin
 * @since 2023/10/12 11:26
 */
@Slf4j
public class DateTest {

    @Test
    public void localDateTest() {
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate = LocalDate.parse("20230705", yyyyMMdd);
        int year = localDate.getYear();
        int monthValue = localDate.getMonthValue();
        int dayOfMonth = localDate.getDayOfMonth();

        log.info(year + "-" + monthValue + "-" + dayOfMonth);
    }
}
