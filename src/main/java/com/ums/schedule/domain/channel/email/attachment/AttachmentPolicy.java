package com.ums.schedule.domain.channel.email.attachment;

import com.ums.schedule.domain.channel.email.exception.AttachmentPolicyRequiredException;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachmentPolicy {
    @Column(name = "attachment_name", nullable = false)
    private String attachmentName;
    @Column(name = "download_name", nullable = false)
    private String downloadName;

    public static AttachmentPolicy of(String attachmentName, String downloadName) {
        AttachmentPolicy attachmentPolicy = new AttachmentPolicy(attachmentName, downloadName);
        attachmentPolicy.validate();
        return attachmentPolicy;
    }

    public void validate() {
        if(!hasAttachmentPolicy()) {
            throw AttachmentPolicyRequiredException.ofDownloadOrAttachmentName();
        }
    }

    private boolean hasAttachmentPolicy() {
        return StringUtils.hasText(attachmentName) && StringUtils.hasText(downloadName);
    }
}
