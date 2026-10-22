package com.ums.schedule.email.application.attachment.model;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.email.adaptor.in.rest.message.request.AttachmentRequest;
import com.ums.schedule.orchestrator.adaptor.out.storage.AwsS3FileMetadataResponse;

public record AttachmentFindResult(
        EnumMapperValue type,
        String fileKey,
        String contentType,
        Long fileSize
) {
    public static AttachmentFindResult of(EnumMapperValue type, AwsS3FileMetadataResponse response) {
        return new AttachmentFindResult(type, response.key(), response.contentType(), response.contentLength());
    }

    public static AttachmentFindResult of(EnumMapperValue type, String fileKey) {
        return new AttachmentFindResult(type, fileKey, null, null);
    }
}
