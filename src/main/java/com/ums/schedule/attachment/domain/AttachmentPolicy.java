package com.ums.schedule.attachment.domain;

import com.ums.schedule.attachment.exception.AttachmentPolicyRequiredException;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import static com.ums.schedule.attachment.exception.AttachmentPolicyRequiredException.ofDownloadOrAttachmentName;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AttachmentPolicy {
    private String attachmentName;
    private String downloadName;

    public static AttachmentPolicy of(String attachmentName, String downloadName) {
        AttachmentPolicy policy = new AttachmentPolicy(attachmentName, downloadName);
        return policy.hasAttachmentPolicy() ? policy : null;
    }

    public void validate() {
        if(!hasAttachmentPolicy()) {
            throw ofDownloadOrAttachmentName();
        }
    }

    private boolean hasAttachmentPolicy() {
        return StringUtils.hasText(attachmentName) && StringUtils.hasText(downloadName);
    }
}
