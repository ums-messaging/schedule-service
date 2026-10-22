package com.ums.schedule.email.domain.common;

import org.springframework.util.StringUtils;

public record MultiPartFile(
        String fileKeyTemplate,
        String attachmentName,
        String downloadName
) {
    public void validateAttachmentName() {
        if(!hasAttachmentName()) {
            throw new IllegalArgumentException();
        }
    }
    public void validateDownloadName() {
        if(!hasDownloadName()) {
            throw new IllegalArgumentException();
        }
    }

    public boolean hasAttachmentAndDownloadName() {
        return hasAttachmentName() && hasDownloadName();
    }

    private boolean hasAttachmentName() {
        return StringUtils.hasText(attachmentName);
    }

    private boolean hasDownloadName() {
        return StringUtils.hasText(downloadName);
    }

    public boolean hasFileKeyTemplate() {
        return StringUtils.hasText(fileKeyTemplate);
    }

    public void validateFileKeyTemplate() {
        if(!hasFileKeyTemplate()) {
            throw new IllegalArgumentException();
        }
    }
}
