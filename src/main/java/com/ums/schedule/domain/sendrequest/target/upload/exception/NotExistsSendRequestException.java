package com.ums.schedule.domain.sendrequest.target.upload.exception;

public class NotExistsSendRequestException extends TargetUploadReportCreateException {

    protected NotExistsSendRequestException(String message) {
        super(message);
    }

    public static NotExistsSendRequestException of() {
        return new NotExistsSendRequestException("발송 요청 데이터가 존재하지 않습니다.");
    }
}
