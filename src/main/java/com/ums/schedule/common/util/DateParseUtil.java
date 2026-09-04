package com.ums.schedule.common.util;

import com.ums.schedule.common.code.common.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DateParseUtil {
    public static String to(LocalDate date, DateTimeFormat format) {
        return date.format(DateTimeFormatter.ofPattern(format.value()));
    }

    public static String to(LocalDateTime date, DateTimeFormat format) {
        return date.format(DateTimeFormatter.ofPattern(format.value()));
    }
}
