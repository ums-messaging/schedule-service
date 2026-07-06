package com.ums.schedule.common.util;

import java.io.File;
import java.util.Arrays;

public abstract class FileUtil {
    private static final String SEPARATOR = File.separator;

    public static String generateFilePaths(String... paths) {
        StringBuffer buffer = new StringBuffer();
        for (String path : paths) {
            buffer.append(path);
            buffer.append(SEPARATOR);
        }
        return buffer.toString();
    }
}
