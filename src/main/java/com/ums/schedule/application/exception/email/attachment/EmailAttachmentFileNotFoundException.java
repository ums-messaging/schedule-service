package com.ums.schedule.application.exception.email.attachment;

import com.ums.schedule.application.exception.ApplicationException;

public class EmailAttachmentFileNotFoundException extends ApplicationException {
    protected EmailAttachmentFileNotFoundException(String message) {
        super(message);
    }

    public static EmailAttachmentFileNotFoundException of(String fileKey) {
        return new EmailAttachmentFileNotFoundException("[%s] 파일이 존재하지 않습니다. ".formatted(fileKey));
    }
}
