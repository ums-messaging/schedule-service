package com.ums.schedule.application.ums.email.generator.handler;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.ums.schedule.application.ums.email.exception.EmailMessageConvertException;
import com.ums.schedule.application.ums.email.generator.handler.model.EmailConvertContext;
import com.ums.schedule.common.code.api.EmailMessageErrorCode;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class PdfConvertHandler implements EmailConvertHandler {

    @Override
    public boolean supports(ConvertType convertType, EmailType emailType) {
        return convertType == ConvertType.PDF && emailType == EmailType.PLAIN;
    }

    @Override
    public File handle(EmailConvertContext context) throws IOException {
        if(context.fileName().endsWith(ConvertType.PDF.description())) {
            if(!StringUtils.hasText(context.template())) {
                throw EmailMessageConvertException.of(EmailMessageErrorCode.NOT_FOUND_CONVERTED_CONTENT);
            }
            Path path = context.path();
            OutputStream os = new FileOutputStream(path.toFile());
            PdfRendererBuilder builder = new PdfRendererBuilder();

            builder.withHtmlContent(context.template(), null);
            builder.toStream(os);
            builder.run();

            return path.toFile();
        }
        throw EmailMessageConvertException.of(EmailMessageErrorCode.INVALID_CONVERT_TYPE_FILE, ConvertType.PDF);
    }
}