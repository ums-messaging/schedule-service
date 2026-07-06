package com.ums.schedule.application.sendrequest.message.email.pipeline;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;
import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@RequiredArgsConstructor
public class PdfConversionPipeline implements MessageGenerationPipeline {
    private final HtmlRendererPipeline handler;

    @Override
    public File handle(EmailAttachment message, String html, SendTarget target) throws IOException {
        File file = this.handler.handle(message, html, target);
        if(message.getConvertType() == ConvertTypeEnum.PDF) {
            File toPdf = File.createTempFile("", ConvertTypeEnum.PDF.code().toLowerCase());
            try (
                    OutputStream os = new FileOutputStream(toPdf)
            ) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
//                builder.withFile(file);
                builder.withHtmlContent(html, null);
                builder.toStream(os);
                builder.run();
            }
            return toPdf;
        }
        return file;
    }

}