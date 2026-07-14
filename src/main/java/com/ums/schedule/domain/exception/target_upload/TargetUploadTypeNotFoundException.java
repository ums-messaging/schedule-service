package com.ums.schedule.domain.exception.target_upload;

public class TargetUploadTypeNotFoundException extends TargetUploadPolicyViolationException {

    protected TargetUploadTypeNotFoundException(String message) {
        super(message);
    }

    public static TargetUploadTypeNotFoundException of() {
        return new TargetUploadTypeNotFoundException("업로드 유형이 존재하지 않습니다.");
    }
}
