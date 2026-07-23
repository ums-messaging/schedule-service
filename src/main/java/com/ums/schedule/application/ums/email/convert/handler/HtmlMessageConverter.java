package com.ums.schedule.application.ums.email.convert.handler;

import com.ums.schedule.application.message.email.model.AttachmentPipelineCommand;
import com.ums.schedule.application.message.email.result.TemplateConversionResult;
import com.ums.schedule.application.ums.common.exception.TemplatePolicyViolationException;
import com.ums.schedule.application.ums.email.exception.EmailMessageConvertException;
import com.ums.schedule.application.ums.email.template.EmailTemplateLoader;
import com.ums.schedule.common.code.email.ConvertType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HtmlMessageConverter implements AttachmentConverter {
    private final EmailTemplateLoader templateLoader;

    @Override
    public TemplateConversionResult handle(AttachmentPipelineCommand command) {
        File file;
        String templateContent;

        try {
            Path path = Files.createTempFile("%s_".formatted(command.filePrefix()), ".%s".formatted(command.fileSuffix()));
            templateContent = templateLoader.loadAndCompileTemplate(command.fileKey(), command.targetDataParam());
            file = validateContentAndWriteFileByConvertType(command.fileKey(), path, command.convertType(), templateContent);
        } catch (Exception e) {
            throw EmailMessageConvertException.of(command.id(), e);
        }
        return TemplateConversionResult.of(file, command.objectKey(), templateContent);
    }

    private File validateContentAndWriteFileByConvertType(String fileKey, Path path, ConvertType convertType, String templateContent) throws IOException {
        validateTemplateContent(fileKey, templateContent);
        if(convertType == ConvertType.HTML) {
            path = Files.writeString(path, templateContent, StandardCharsets.UTF_8);
        }
        return Optional.ofNullable(path)
                .map(p -> p.toFile())
                .orElse(path.toFile());
    }

    private void validateTemplateContent(String fileKey, String templateContent) {
        if(!StringUtils.hasText(templateContent)) {
            throw TemplatePolicyViolationException.of(fileKey);
        }
    }

    @Override
    public boolean supports(ConvertType convertType, boolean isSecurity) {
        return convertType == ConvertType.HTML && isSecurity == false;
    }
}
