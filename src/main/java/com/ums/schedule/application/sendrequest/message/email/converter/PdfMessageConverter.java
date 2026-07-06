package com.ums.schedule.application.sendrequest.message.email.converter;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.ums.schedule.application.sendrequest.message.email.command.AttachmentPipelineCommand;
import com.ums.schedule.application.exception.EmailMessageConvertException;
import com.ums.schedule.application.sendrequest.message.email.result.TemplateConversionResult;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@RequiredArgsConstructor
public class PdfMessageConverter implements AttachmentConverter {
    private final HtmlMessageConverter handler;

    @Override
    public boolean supports(ConvertTypeEnum convertType, boolean isSecurity) {
        return convertType == ConvertTypeEnum.PDF && isSecurity == false;
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