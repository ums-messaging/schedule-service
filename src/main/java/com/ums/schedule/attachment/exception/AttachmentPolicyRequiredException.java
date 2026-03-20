package com.ums.schedule.attachment.exception;

public class AttachmentPolicyRequiredException extends AttachmentException {
    protected AttachmentPolicyRequiredException(String message) {
        super(message);
    }

    public static AttachmentPolicyRequiredException ofDownloadOrAttachmentName() {
        return new AttachmentPolicyRequiredException("download name or attachment name ");
    }
}
