package com.ums.schedule.orchestrator.application.upload.result;

import com.ums.schedule.orchestrator.adaptor.out.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.orchestrator.domain.upload.SendRequestUpload;

public record FileUploadRequestResult(
        SendRequestUpload sendRequestUpload,
        String contentType,
        Long fileSize
) {
    public static FileUploadRequestResult of(SendRequestUpload sendRequestUpload, AwsS3FileMetadataResponse response) {
        return new FileUploadRequestResult(
                sendRequestUpload,
                response.contentType(),
                response.contentLength()
        );
    }
}
