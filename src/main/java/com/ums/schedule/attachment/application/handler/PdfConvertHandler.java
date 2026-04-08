package com.ums.schedule.attachment.application.handler;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.send.application.model.dto.SendTargetDto;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@RequiredArgsConstructor
public class PdfConvertHandler implements EmailBodyHandler {
    private final @Qualifier("htmlUploadHandler")
    EmailBodyHandler handler;

    @Override
    public File handle(Attachment attachment, Template template, SendTargetDto targetDto) throws IOException {
        File file = this.handler.handle(attachment, template, targetDto);
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
//        AttachmentPolicy policy = AttachmentPolicy.of(body.attachmentName(), body.downloadName());
//        Map<String, Object> targetData = targetDto.extractMessageVariable();
    }

}