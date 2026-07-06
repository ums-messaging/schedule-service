package com.ums.schedule.application.send.mime;

import com.ums.schedule.domain.send.email.mime.MimeMessage;
import com.ums.schedule.domain.send.email.mime.MultipartImage;
import com.ums.schedule.adapter.api.email.smtp.response.SmtpSessionInfo;
import com.ums.schedule.adapter.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.adapter.storage.AwsS3Repository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

@Component
public class MimeImageWriter extends MimeMessageWriter {
    private final AwsS3Repository repository;

    public MimeImageWriter(@Qualifier("mimeHtmlWriter") MimeWriter writer, AwsS3Repository repository) {
        super(writer);
        this.repository = repository;
    }

    @Override
    public String write(SmtpSessionInfo session, MimeMessage mimeMessage) throws IOException {
        if (mimeMessage.images().length > 0) {
            session.sendCommand("Content-Type: multipart/related; boundary=\"--related-boundary\"");
            session.sendCommand("--related-boundary");
            super.write(session, mimeMessage);


            for (MultipartImage image : mimeMessage.images()) {
                AwsS3FileMetadataResponse fileMetadata = repository.getFileMetadata(image.fileKey());
                session.sendCommand("-- related-boundary");
                session.sendCommand("Content-Type: " + fileMetadata.contentType());
                session.sendCommand("Content-Transfer-Encoding: base64");
                session.sendCommand("Content-ID: <" + image.contentId() +">");
                session.sendCommand("Content-Disposition: inline; filename=\""+image.fileKey()+"\"");

                InputStream is = repository.getFileContent(image.fileKey());
                OutputStream wrapOs = Base64.getMimeEncoder().wrap(session.os());
                is.transferTo(wrapOs);
                wrapOs.close();
            }
            session.sendCommand("--related-boundary--");
        }
        return session.readLine();
    }
}
