package com.ums.schedule.application.send.mime;

import com.ums.schedule.domain.send.email.mime.MimeMessage;
import com.ums.schedule.adapter.api.email.smtp.response.SmtpSessionInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component(value = "mimeHtmlWriter")
public class MimeHtmlWriter implements MimeWriter {

    @Override
    public String write(SmtpSessionInfo session, MimeMessage mimeMessage) throws IOException {
        StringBuilder builder = new StringBuilder();

        session.sendCommand("Content-Type: \"multipart/alternative;\"; boundary=alt-boundary");
        session.sendCommand("-- alt-boundary");
        session.sendCommand("Content-Type: text/html; charset=UTF-8");
        session.sendCommand("Content-Transfer-Encoding: 8bit");
        session.sendCommand(mimeMessage.body());
        session.sendCommand("--alt-boundary--");

        return builder.toString();
    }
}
