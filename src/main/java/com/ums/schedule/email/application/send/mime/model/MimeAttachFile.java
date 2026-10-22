package com.ums.schedule.email.application.send.mime.model;

import com.ums.schedule.common.util.TemplateUtil;
import com.ums.schedule.email.application.message.generator.policy.model.AttachMessage;
import com.ums.schedule.email.application.send.mime.writer.MimeWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public record MimeAttachFile(
        AttachMessage message,
        Path file
) {
    public static MimeAttachFile of(AttachMessage message, Path file) {
        return new MimeAttachFile(message, file);
    }

    public static MimeAttachFile of(AttachMessage attachMessage) {
        return new MimeAttachFile(attachMessage, null);
    }

    public void setMultipartHeader(MimeWriter writer, Map<String, Object> targetData) throws IOException {
        String attachmentName = TemplateUtil.parse(message.attachmentName(), targetData);
        writer.write("-- mixed-boundary");
        writer.write( "Content-Type: "+ message.contentType() + "; name=\""+attachmentName+"\"");
        writer.write( "Content-Transfer-Encoding: base64");
    }

    public void setAttachmentAndDownloadName(MimeWriter writer, Map<String, Object> targetData) throws IOException {
        String downloadName = TemplateUtil.parse(message.downloadName(), targetData);
        writer.write("Content-Disposition: attachment; filename=\""+downloadName+"\"");
    }
}
