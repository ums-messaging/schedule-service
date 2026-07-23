package com.ums.schedule.common.exception.file;

import com.ums.schedule.common.code.api.ErrorCode;
import com.ums.schedule.common.code.api.FileErrorCode;
import com.ums.schedule.common.code.mapper.EnumMapperType;
import com.ums.schedule.common.exception.InternalServerException;

public class FilePathGeneratedException extends InternalServerException {
    protected FilePathGeneratedException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }

    public static FilePathGeneratedException of(FileErrorCode errorCode, Throwable e) {
        return new FilePathGeneratedException(errorCode, e);
    }
}
