package com.ums.schedule.attachment.application.converter.handler;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.ums.schedule.attachment.application.model.AttachmentDto;
import com.ums.schedule.send.domain.request.EmailBody;
import com.ums.schedule.send.domain.target.EmailSendTarget;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;
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
    public File handle(AttachmentDto attachment, EmailBody body, EmailSendTarget target) throws IOException {
        File file = this.handler.handle(attachment, body, target);
        if(attachment.getConvertType() == ConvertTypeEnum.PDF) {
            File toPdf = File.createTempFile("", ".pdf");
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