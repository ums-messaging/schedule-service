package com.ums.schedule.domain.target.exeption.upload;

import com.ums.schedule.code.send.TargetUploadStatusEnum;

public class TargetUploadCompleteStateException extends TargetUploadStateException {

    protected TargetUploadCompleteStateException(TargetUploadStatusEnum to) {
        super(TargetUploadStatusEnum.COMPLETED, to);
    }

    protected TargetUploadCompleteStateException(String message) {
        super(message);
    }

    public static TargetUploadCompleteStateException of(TargetUploadStatusEnum to) {
        return new TargetUploadCompleteStateException(to);
    }

    public static TargetUploadCompleteStateException ofDifferentTargetSize(int count, int uploadCount) {
        return new TargetUploadCompleteStateException(String.format("대상자 업로드 완료 (카운팅 수 : %d, 업로드 수 : %d) ", count, uploadCount));
    }

    public static TargetUploadCompleteStateException targetUploadUnComplete() {
        return new TargetUploadCompleteStateException("업로드가 완료되지 않은 요청입니다.");
    }
}