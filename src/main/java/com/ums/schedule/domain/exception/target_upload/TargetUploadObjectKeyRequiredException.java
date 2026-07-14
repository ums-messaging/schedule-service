package com.ums.schedule.domain.exception.target_upload;

public class TargetUploadObjectKeyRequiredException extends TargetUploadRequiredException {

    protected TargetUploadObjectKeyRequiredException(String message) {
        super(message);
    }

    public static TargetUploadObjectKeyRequiredException of() {
        return new TargetUploadObjectKeyRequiredException("Object Key");
    }
}
