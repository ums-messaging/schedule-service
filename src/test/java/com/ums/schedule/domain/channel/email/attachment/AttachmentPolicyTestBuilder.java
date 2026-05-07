package com.ums.schedule.domain.channel.email.attachment;

public class AttachmentPolicyTestBuilder {
    private String attachmentName = "attachmentName";
    private String downloadName = "downloadName";

    public static AttachmentPolicyTestBuilder builder() {
        return new AttachmentPolicyTestBuilder();
    }

    public AttachmentPolicyTestBuilder attachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public AttachmentPolicyTestBuilder downloadName(String downloadName) {
        this.downloadName = downloadName;
        return this;
    }

    public AttachmentPolicy build() {
        return new AttachmentPolicy(attachmentName, downloadName);
    }
}
