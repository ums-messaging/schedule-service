package com.ums.schedule.common.util;

import com.ums.schedule.common.code.DateTimeFormatEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DateParseUtil {
    public static String to(LocalDate date, DateTimeFormatEnum format) {
        return date.format(DateTimeFormatter.ofPattern(format.value()));
    }

    public static String to(LocalDateTime date, DateTimeFormatEnum format) {
        return date.format(DateTimeFormatter.ofPattern(format.value()));
    }
}
