package com.ums.schedule.attachment.application.converter.handler;

import com.ums.schedule.attachment.application.model.AttachmentDto;
import com.ums.schedule.send.domain.request.EmailBody;
import com.ums.schedule.send.domain.target.EmailSendTarget;

import java.io.File;
import java.io.IOException;

/**
 * 이메일 바디 기준으로 해야함
 *
 * */
// target정보 세팅하면서
public interface EmailBodyConvertHandler {
    File handle(AttachmentDto attachment, EmailBody body, EmailSendTarget target) throws IOException;
}
