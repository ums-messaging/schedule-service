package com.ums.schedule.application.channel.email.converter.handler;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.ums.schedule.application.channel.email.dto.AttachmentDto;
import com.ums.schedule.domain.channel.email.message.EmailBody;
import com.ums.schedule.domain.target.SendTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@RequiredArgsConstructor
public class PdfConvertHandler implements EmailBodyConvertHandler {
    private final @Qualifier("htmlUploadHandler")
    EmailBodyConvertHandler handler;

    @Override
    public File handle(AttachmentDto attachment, EmailBody body, SendTarget target) throws IOException {
        File file = this.handler.handle(attachment, body, target);
        if(body.shouldConvertToPdf()) {
            File toPdf = File.createTempFile("", body.getUploadFileExt());
            try (
                    OutputStream os = new FileOutputStream(toPdf)
            ) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.withFile(file);
                builder.toStream(os);
                builder.run();
            }
            return toPdf;
        }
        return file;
    }

}