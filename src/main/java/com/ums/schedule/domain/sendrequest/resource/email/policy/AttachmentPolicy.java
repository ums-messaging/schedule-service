package com.ums.schedule.domain.sendrequest.resource.email.policy;

import com.ums.schedule.common.util.ValidationUtils;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachmentPolicy {
    @Column(name = "attachment_name", nullable = false)
    private String attachmentName;

    @Column(name = "download_name", nullable = false)
    private String downloadName;

    public static AttachmentPolicy of(String attachmentName, String downloadName) {
        AttachmentPolicy attachmentPolicy = new AttachmentPolicy();
        attachmentPolicy.assignAttachmentName(attachmentName);
        attachmentPolicy.assignDownloadName(downloadName);
        return attachmentPolicy;
    }

    private void assignDownloadName(String downloadName) {
        ValidationUtils.isEmpty("download_name", downloadName);
        this.downloadName = downloadName;
    }

    private void assignAttachmentName(String attachmentName) {
        ValidationUtils.isEmpty("attachment_name", attachmentName);
        this.attachmentName = attachmentName;
    }


}
