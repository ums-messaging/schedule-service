package com.ums.schedule.email.adaptor.in.rest.message.request;

import jakarta.validation.constraints.NotBlank;

public record AttachmentRequest(
        String type,
        String fileKey,
        @NotBlank(message = "EMAIL_SEND_REQUEST:ATTACHMENT_NAME_REQUIRED")
        String attachmentName,
        @NotBlank(message = "EMAIL_SEND_REQUEST:DOWNLOAD_NAME_REQUIRED")
        String downloadName
) {

}
