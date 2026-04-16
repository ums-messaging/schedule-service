package com.ums.schedule.domain.request.exception;

public class TargetUploadException extends SendRequestException {
    public TargetUploadException(String message) {
        super(message);
    }

    public static TargetUploadException addTargetUpload() {
        return new TargetUploadException("이미 발송 요청된 건에 대해서 대상자를 추가할 수 없습니다.");
    }
}
