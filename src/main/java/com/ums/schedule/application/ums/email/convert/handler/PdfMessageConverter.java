package com.ums.schedule.application.ums.email.convert.handler;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.ums.schedule.application.message.email.model.AttachmentPipelineCommand;
import com.ums.schedule.application.message.email.result.TemplateConversionResult;
import com.ums.schedule.application.exception.email.EmailMessageConvertException;
import com.ums.schedule.common.code.email.ConvertType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@RequiredArgsConstructor
public class PdfMessageConverter implements AttachmentConverter {
    private final HtmlMessageConverter handler;

    @Override
    public boolean supports(ConvertType convertType, boolean isSecurity) {
        return convertType == ConvertType.PDF && isSecurity == false;
    }

    @Override
    public TemplateConversionResult handle(AttachmentPipelineCommand command) throws EmailMessageConvertException {
        TemplateConversionResult result;
        try {
            result = this.handler.handle(command);
            OutputStream os = new FileOutputStream(result.tempFile());
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(result.templateContent(), null);
            builder.toStream(os);
            builder.run();
        } catch (IOException e) {
            throw EmailMessageConvertException.of(e);
        }
        return result;
    }

}