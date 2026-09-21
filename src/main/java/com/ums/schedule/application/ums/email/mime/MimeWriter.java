package com.ums.schedule.application.ums.email.mime;

import com.ums.schedule.domain.send.email.mime.MimeMessage;
import com.ums.schedule.adapter.api.email.smtp.response.SmtpSessionInfo;

import java.io.IOException;

public interface MimeWriter {
    String write(SmtpSessionInfo session, MimeMessage mimeMessage) throws IOException;
}
