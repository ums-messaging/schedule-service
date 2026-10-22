package com.ums.schedule.email.application.send.mime.model;

import com.ums.schedule.email.adaptor.out.smtp.model.SmtpSessionInfo;
import com.ums.schedule.email.application.send.job.model.SendJob;
import com.ums.schedule.email.application.send.mime.writer.MimeWriter;
import com.ums.schedule.common.util.TemplateUtil;
import com.ums.schedule.email.application.send.job.model.MessageSendData;
import com.ums.schedule.email.application.send.loader.model.TemplateLoadResult;
import com.ums.schedule.email.domain.requests.sender.Sender;
import com.ums.schedule.email.domain.target.SendTargetRequest;
import com.ums.schedule.email.domain.target.receiver.Receiver;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record MimeMessage(
        String domain,
        String from,
        String fromName,
        String subject,
        Path body,
        List<MultiPartImage> images,
        List<MimeAttachFile> attachments
) {
    public static MimeMessage of(SendJob job, String domain, TemplateLoadResult template) {
        MessageSendData message = job.messageData();
        Sender sender = job.sender();
        return new MimeMessage(
                domain,
                sender.from(),
                sender.fromName(),
                message.subject(),
                template.bodyMessage(),
                template.images(),
                template.files()
        );
    }

    public void setMailFrom(Writer writer) {
        write(writer, "From: " + from);
    }
    public void setTo(Writer writer, String receiver) {
        write(writer, "To: " + receiver);
    }

    public void setMimeVersion(Writer writer, String version) {
        write(writer, "MIME-Version: " + version);
    }

    public void setDate(Writer writer) {
        write(writer, "Date: "+ LocalDateTime.now());
    }

    public void setSubject(Writer writer, Map<String, Object> dataParam) {
        String subject = TemplateUtil.parse(this.subject, dataParam);
        write(writer,"Subject: "+ subject);
    }

    private void write(Writer writer, String command) {
        try {
            writer.write(command);
            writer.write("\r\n");
        } catch (IOException e) {
        }
    }

    public void setBody(MimeWriter writer, Template template, Map<String, Object> targetData) throws TemplateException, IOException {
        writer.write("Content-Type: \"multipart/alternative;\"; boundary=\"alt-boundary\"");
        writer.write("\r\n");
        writer.write("-- alt-boundary");
        writer.write("Content-Type: text/html; charset=UTF-8");
        writer.write( "Content-Transfer-Encoding: 8bit");
        writer.write(template, targetData);
        writer.write("--alt-boundary--");
    }

    public void setImageHeader(MimeWriter writer) throws IOException {
        writer.write("Content-Type: multipart/related; boundary=\"--related-boundary\"");
        writer.write("--related-boundary");
        writer.write("\r\n");
    }

    public void setImageFooter(MimeWriter writer) throws IOException {
        writer.write("--related-boundary--");
        writer.write("\r\n");
    }

    public void setMultipartHeader(MimeWriter writer) throws IOException {
        writer.write("Content-Type: multipart/mixed; boundary=\"mixed-boundary\"");
        writer.write("-- mixed-boundary");
    }

    public void setMultipartFooter(MimeWriter writer) throws IOException {
        writer.write("-- mixed-boundary--");
    }

}
