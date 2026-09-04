package com.ums.schedule.adapter.api.request.email.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record EmailAttachmentListRequest(
        @NotBlank(message = "ATTACHMENT:ATTACHMENT_UPLOAD_KEY_REQUIRED")
        String attachmentUploadKey,
        @Valid @Size(min = 1, message = "ATTACHMENT:ATTACHMENT_LIST_EMPTY")
        List<EmailAttachmentRequest> list
) {
}
