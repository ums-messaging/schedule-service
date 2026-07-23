package com.ums.schedule.common.exception.file;

import com.ums.schedule.common.code.api.ErrorCode;
import com.ums.schedule.common.code.api.FileErrorCode;

public class FileNotFoundException extends FileStorageException {
    protected FileNotFoundException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public static FileNotFoundException of(String fileKey) {
        return new FileNotFoundException(FileErrorCode.FILE_NOT_FOUND, fileKey);
    }
}
