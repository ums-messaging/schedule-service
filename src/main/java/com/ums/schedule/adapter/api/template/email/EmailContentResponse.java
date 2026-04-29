package com.ums.schedule.adapter.api.template.email;


import com.ums.schedule.domain.channel.email.attachment.AttachmentPolicy;
import com.ums.schedule.domain.channel.email.attachment.FileMetaData;
import com.ums.schedule.domain.channel.email.message.EmailBody;

public record EmailContentResponse(
        String contentId,
        String section,
        String format,
        String content,
        String contentType,
        String attachmentName,
        String downloadName,
        String storageType,
        String baseDir,
        String fileKey,
        String fileUrl,
        String originalFileName,
        Long fileSize
) {
    public FileMetaData getFileMetaData() {
        return FileMetaData.fromResponse(this);
    }

    public AttachmentPolicy getAttachmentPolicy() {
        return AttachmentPolicy.of(this.attachmentName, this.downloadName);
    }

    public void getBody(EmailBody body) {

    }
}
