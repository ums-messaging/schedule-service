package com.ums.schedule.domain.channel.email.attachment;

import com.ums.schedule.domain.channel.email.exception.AttachmentPolicyRequiredException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AttachmentPolicy {
    private String attachmentName;
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
