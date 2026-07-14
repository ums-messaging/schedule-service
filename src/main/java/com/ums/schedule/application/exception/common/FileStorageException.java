package com.ums.schedule.application.exception.common;

import com.ums.schedule.application.exception.ApplicationException;

public class FileStorageException extends ApplicationException {
    protected FileStorageException(Throwable cause) {
        super(cause);
    }

    public static FileStorageException of(Throwable cause) {
        return new FileStorageException(cause);
    }
}
