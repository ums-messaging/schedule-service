package com.ums.schedule.common.util;

import org.springframework.util.StringUtils;

import java.util.Optional;

public abstract class ValueResolverUtils {
    public static String getValueOrDefault(String commandValue, String defaultValue) {
        return Optional.ofNullable(commandValue)
                .filter(StringUtils::hasText)
                .orElse(ValueResolverUtils.getValueOrDefault(defaultValue, null));
    }
}
