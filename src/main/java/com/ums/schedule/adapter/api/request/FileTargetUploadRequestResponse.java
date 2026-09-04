package com.ums.schedule.adapter.api.request;

import com.ums.schedule.application.target.processor.model.FileTargetUploadRequestResult;

public record FileTargetUploadRequestResponse(
     Long requestId,
     String status,
     String uploadId
) {
    public static FileTargetUploadRequestResponse of(Long requestId, FileTargetUploadRequestResult result) {
        return new FileTargetUploadRequestResponse(
                requestId,
                result.status().description(),
                result.id().toString()
        );
    }
}
