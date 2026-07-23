package com.ums.schedule.common.exception.file;

import com.ums.schedule.common.code.api.ErrorCode;
import com.ums.schedule.common.exception.ExternalSystemException;

public abstract class FileStorageException extends ExternalSystemException {
    protected FileStorageException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }


}
