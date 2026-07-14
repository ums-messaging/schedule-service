package com.ums.schedule.domain.exception.target_upload;

import com.ums.schedule.domain.exception.target_upload.TargetUploadException;

public abstract class TargetUploadEventException extends TargetUploadException {

    protected TargetUploadEventException(String message) {
        super(String.format("%s 처리 중 오류가 발생했습니다.", message));
    }
}
