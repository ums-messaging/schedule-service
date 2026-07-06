package com.ums.schedule.domain.sendrequest.target.upload.exception;

public class UploadTypeRequiredException extends TargetUploadReportCreateException {

    protected UploadTypeRequiredException(String message) {
        super(message);
    }

    public static UploadTypeRequiredException of() {
        return new UploadTypeRequiredException("upload_type은 필수 값 입니다.");
    }
}
