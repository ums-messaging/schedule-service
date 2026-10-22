package com.ums.schedule.orchestrator.application.upload.result;

import com.ums.schedule.orchestrator.domain.upload.SendRequestUpload;

public record SendRequestUploadRequestResult(
        Long id,
        String contentType,
        String status,
        String objectKey,
        Long fileSize
) {

    public static SendRequestUploadRequestResult of(SendRequestUpload upload) {
        return new SendRequestUploadRequestResult(
                upload.getId(),
                upload.contentType(),
                upload.getState().getCurrentCode().code(),
                upload.objectKey(),
                upload.fileSize()
        );
    }
}
