package com.ums.schedule.email.adaptor.in.rest.message.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.*;

public record MessageCreateRequest(
        String templateId,
        String title,
        String description,
        String messageType,
        @NotBlank(message = "EMAIL_SEND_REQUEST:MAIL_TYPE_REQUIRED")
        String mailType,
        @NotBlank(message = "SEND_REQUEST:SENDER_KEY_REQUIRED")
        String convertType,
        @NotBlank(message = "EMAIL_SEND_REQUEST:TITLE_REQUIRED")
        String subject,
        String attachmentName,
        String downloadName,
        String fileKeyTemplate,
        @Valid
        SecurityMailRequest securityPolicy,
        @Valid
        List<AttachmentRequest> attachmentList
        )
{
        public String passwordFormat() {
                return Optional.ofNullable(securityPolicy)
                        .map(SecurityMailRequest::passwordFormat)
                        .orElse(null);
        }
}
