package com.ums.schedule.attachment.application.response;

public record AttachmentResponse(
    String resourceId,
    String resourceName,
    String storageType,
    String contentType,
    String baseDir,
    String fileOriginalName,
    String fileKey,
    String fileUrl,
    Long fileSize,
    String attachmentName,
    String downloadName
) {
}
