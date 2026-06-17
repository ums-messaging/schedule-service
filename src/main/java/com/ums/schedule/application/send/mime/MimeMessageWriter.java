package com.ums.schedule.application.send.mime;

import com.ums.schedule.domain.send.email.mime.MimeMessage;
import com.ums.schedule.adapter.api.email.smtp.response.SmtpSessionInfo;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class MimeMessageWriter implements MimeWriter {
    private final MimeWriter writer;

    @Override
    public String write(SmtpSessionInfo session, MimeMessage mimeMessage) throws IOException {
        return writer.write(session, mimeMessage);
    }
}
