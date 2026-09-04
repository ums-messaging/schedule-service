package com.ums.schedule.common.util;

import com.ums.schedule.common.code.api.FileErrorCode;
import com.ums.schedule.common.exception.file.FilePathGeneratedException;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Arrays;

public abstract class FileUtil {

    public static String generateFilePaths(String... paths) {
        StringBuffer buffer = new StringBuffer();
        try {
            for (String path : paths) {
                if(StringUtils.hasText(path)) {
                    buffer.append(path);
                    buffer.append("/");
                }
            }
            return buffer.toString().substring(0, buffer.length()-1);
        } catch (Exception e) {
            throw FilePathGeneratedException.of(FileErrorCode.FILE_GENERATED_FAIL, e);
        }
    }
}
