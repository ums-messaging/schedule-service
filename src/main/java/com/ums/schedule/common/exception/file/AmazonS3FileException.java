package com.ums.schedule.common.exception.file;

import com.ums.schedule.common.code.api.FileErrorCode;
import software.amazon.awssdk.awscore.exception.AwsServiceException;

public class AmazonS3FileException extends FileStorageException {
    protected AmazonS3FileException(FileErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public static AmazonS3FileException of(FileErrorCode errorCode, String fileKey, Throwable e) {
        return new AmazonS3FileException(errorCode, fileKey, e.getMessage());
    }
}
