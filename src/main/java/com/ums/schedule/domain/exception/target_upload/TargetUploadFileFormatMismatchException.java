package com.ums.schedule.domain.exception.target_upload;

import com.ums.schedule.common.code.target_upload.TargetUploadFormatEnum;

public class TargetUploadFileFormatMismatchException extends TargetUploadPolicyViolationException {
    protected TargetUploadFileFormatMismatchException(TargetUploadFormatEnum format) {
        super("%s 확장자의 파일만 업로드 가능합니다.".formatted(format.code()));
    }

    public static TargetUploadFileFormatMismatchException of(TargetUploadFormatEnum format) {
        return new TargetUploadFileFormatMismatchException(format);
    }
}
