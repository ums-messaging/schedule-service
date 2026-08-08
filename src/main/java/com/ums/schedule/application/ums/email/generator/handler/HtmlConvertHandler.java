package com.ums.schedule.application.ums.email.generator.handler;

import com.ums.schedule.application.ums.email.exception.EmailMessageConvertException;
import com.ums.schedule.application.ums.email.generator.handler.model.EmailConvertContext;
import com.ums.schedule.common.code.api.EmailMessageErrorCode;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Component
@RequiredArgsConstructor
public class HtmlConvertHandler implements EmailConvertHandler {

    @Override
    public boolean supports(ConvertType convertType, EmailType emailType) {
        return convertType == ConvertType.HTML;
    }

    public File handle(EmailConvertContext context) throws IOException {
        if(context.path().getFileName().endsWith(ConvertType.HTML.description())) {
            if(!StringUtils.hasText(context.template())) {
                throw EmailMessageConvertException.of(EmailMessageErrorCode.NOT_FOUND_CONVERTED_CONTENT);
            }
            return Files.writeString(context.path(), context.template(), StandardCharsets.UTF_8).toFile();
        }
        throw EmailMessageConvertException.of(EmailMessageErrorCode.INVALID_CONVERT_TYPE_FILE, ConvertType.HTML);
    }
}
