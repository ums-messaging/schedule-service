package com.ums.schedule.domain.sendrequest.target.upload.exception;

public class UnSupportedTargetUploadTypeException extends TargetUploadPolicyViolationException {
    protected UnSupportedTargetUploadTypeException(String message) {
        super(message);
    }

    public static UnSupportedTargetUploadTypeException of() {
        return new UnSupportedTargetUploadTypeException("지원하지 않는 업로드 유형 입니다.");
    }
}
