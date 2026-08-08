package com.ums.schedule.application.send.mime;

import com.ums.schedule.domain.send.email.mime.MimeMessage;
import com.ums.schedule.domain.send.email.mime.MimeMultiPart;
import com.ums.schedule.adapter.api.email.smtp.response.SmtpSessionInfo;
import com.ums.schedule.adapter.storage.AwsS3Repository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

@Component(value = "mimeAttachmentWriter")
public class MimeAttachmentWriter extends MimeMessageWriter {
    private final AwsS3Repository repository;
    public MimeAttachmentWriter(@Qualifier("mimeImageWriter") MimeWriter writer, AwsS3Repository repository) {
        super(writer);
        this.repository = repository;
    }

    @Override
    public String write(SmtpSessionInfo session, MimeMessage mimeMessage) throws IOException {
        if(mimeMessage.attachments().length > 0) {
            StringBuilder builder = new StringBuilder();
            session.sendCommand("Content-Type: multipart/mixed; boundary=\"mixed-boundary\"");
            session.sendCommand("-- mixed-boundary");

            super.write(session, mimeMessage);

            for(MimeMultiPart multiPart : mimeMessage.attachments()) {
                session.sendCommand("-- mixed-boundary");
                session.sendCommand("Content-Type: "+ multiPart.contentType());
                session.sendCommand("Content-Transfer-Encoding: base64");
                session.sendCommand("Content-Disposition: attachment; filename=\""+multiPart.filename()+"\"");

                InputStream is = repository.getFileContent(multiPart.fileKey());
                OutputStream wrapOs = Base64.getMimeEncoder().wrap(session.os());
                is.transferTo(wrapOs);
                wrapOs.close();
            }
            session.sendCommand("-- mixed-boundary--");

            return builder.toString();
        }

        return "";
    }
}
