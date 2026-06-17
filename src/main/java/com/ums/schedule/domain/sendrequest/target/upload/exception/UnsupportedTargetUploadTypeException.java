package com.ums.schedule.domain.sendrequest.target.upload.exception;

public class UnsupportedTargetUploadTypeException extends TargetUploadPolicyException {
    protected UnsupportedTargetUploadTypeException(String message) {
        super(message);
    }

    public static UnsupportedTargetUploadTypeException of() {
        return new UnsupportedTargetUploadTypeException("지원하지 않는 업로드 유형 입니다.");
    }
}
