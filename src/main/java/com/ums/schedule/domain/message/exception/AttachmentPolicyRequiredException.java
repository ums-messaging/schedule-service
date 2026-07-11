package com.ums.schedule.domain.message.exception;

public class AttachmentPolicyRequiredException extends AttachmentException {
    protected AttachmentPolicyRequiredException(String message) {
        super(message);
    }

    public static AttachmentPolicyRequiredException ofDownloadOrAttachmentName() {
        return new AttachmentPolicyRequiredException("download name or attachment name ");
    }

    public static AttachmentPolicyRequiredException ofFileMetadata(String message) {
        return new AttachmentPolicyRequiredException(message);
    }
}
