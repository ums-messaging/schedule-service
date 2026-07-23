package com.ums.schedule.common.util;

import org.springframework.util.StringUtils;

public class ValidationUtils {
    public static void isEmpty(String field, String content) {
        if(!StringUtils.hasText(content)) {
//            throw RequiredException.fieldOf(field);
        }
    }

    public static void isEmpty(String field, Object content) {
        if(content == null) {
//            throw RequiredException.fieldOf(field);
        }
    }
}
