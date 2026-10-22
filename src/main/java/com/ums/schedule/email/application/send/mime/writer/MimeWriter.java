package com.ums.schedule.email.application.send.mime.writer;

import com.ums.schedule.common.util.TemplateUtil;
import com.ums.schedule.email.adaptor.out.smtp.model.SmtpSessionInfo;
import com.ums.schedule.email.application.send.mime.model.MimeMessage;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public record MimeWriter(
        SmtpSessionInfo session,
        MimeMessage mimeMessage,
        Writer writer
) {
//    public static MimeWriter of(SmtpSessionInfo session, MimeMessage mimeMessage) {
//        BufferedWriter writer = new BufferedWriter(
//                new OutputStreamWriter(session.os(), StandardCharsets.UTF_8)
//        );
//        return new MimeWriter(session, mimeMessage, writer);
//    }
    public static MimeWriter of(SmtpSessionInfo session, Writer writer, MimeMessage message) {
        return new MimeWriter(session, message, writer);
    }

    public void write(String command) throws IOException {
        writer.write(command);
        writer.write("\r\n");
//        writer.flush();
    }

    public void write(InputStream is) throws IOException {
        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            reader.transferTo(writer);
//            writer.flush();
        }
    }

    public void write(Template template, Map<String, Object> targetData) throws TemplateException, IOException {
        template.process(targetData, writer);
//        writer.flush();
    }

    public void setMailFrom() {
        session.sendCommand("MAIL FROM: <" + mimeMessage.from() + ">");
        session.expect(250);
    }

    public void setReceiver(String receiver) {
        session.sendCommand("RCPT TO: <" + receiver + ">");
        session.expect(250);
    }

    public void setData() {
        session.sendCommand("DATA");
        session.expect(354);
    }
    public void setHeaders(String receiver, Map<String, Object> targetData) {
        session.sendCommand("From: " + mimeMessage.from());
        session.sendCommand("To: " + receiver);
        setSubject(targetData);
        session.sendCommand("MIME-Version: 1.0");
    }
    private void setSubject(Map<String, Object> targetData) {
        String subject = TemplateUtil.parse(mimeMessage.subject(), targetData);
        session.sendCommand("Subject: " + subject);
    }

    public void setFinish() {
        session.sendCommand(".");
        session.expect(250);
    }
}
