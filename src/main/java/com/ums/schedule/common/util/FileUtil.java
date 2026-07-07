package com.ums.schedule.common.util;

import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Arrays;

public abstract class FileUtil {
    private static final String SEPARATOR = File.separator;

    public static String generateFilePaths(String... paths) {
        StringBuffer buffer = new StringBuffer();

        for (String path : paths) {
            if(StringUtils.hasText(path)) {
                buffer.append(path);
                buffer.append("/");
            }
        }
        return buffer.toString().substring(0, buffer.length()-1);
    }
}
