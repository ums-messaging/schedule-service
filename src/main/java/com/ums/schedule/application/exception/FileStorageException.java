package com.ums.schedule.application.exception;

public class FileStorageException extends ApplicationException {
    protected FileStorageException(Throwable cause) {
        super(cause);
    }

    public static FileStorageException of(Throwable cause) {
        return new FileStorageException(cause);
    }
}
