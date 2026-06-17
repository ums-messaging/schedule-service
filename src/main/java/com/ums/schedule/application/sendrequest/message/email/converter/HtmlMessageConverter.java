package com.ums.schedule.application.sendrequest.message.email.converter;

import com.ums.schedule.application.sendrequest.message.email.command.AttachmentPipelineCommand;
import com.ums.schedule.application.exception.EmailMessageConvertException;
import com.ums.schedule.application.sendrequest.message.email.result.TemplateConversionResult;
import com.ums.schedule.application.sendrequest.template.loader.EmailTemplateLoader;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;
import com.ums.schedule.domain.sendrequest.template.exception.TemplateNotFoundException;
import freemarker.template.TemplateException;
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
    // html 업로드
    @Override
    public TemplateConversionResult handle(AttachmentPipelineCommand command) {
        File file;
        String templateContent;

        try {
            Path path = Files.createTempFile("%s_".formatted(command.filePrefix()), ".%s".formatted(command.fileSuffix()));
            templateContent = templateLoader.loadAndCompileTemplate(command.fileKey(), command.targetDataParam());
            file = validateContentAndWriteFileByConvertType(path, command.convertType(), templateContent);
        } catch (IOException e) {
            throw EmailMessageConvertException.of(e);
        }
        return TemplateConversionResult.of(file, command.objectKey(), templateContent);
    }

    private File validateContentAndWriteFileByConvertType(Path path, ConvertTypeEnum convertType, String templateContent) throws IOException {
        validateTemplateContent(templateContent);
        if(convertType == ConvertTypeEnum.HTML) {
            path = Files.writeString(path, templateContent, StandardCharsets.UTF_8);
        }
        return Optional.ofNullable(path)
                .map(p -> p.toFile())
                .orElse(path.toFile());
    }

    private void validateTemplateContent(String templateContent) {
        if(!StringUtils.hasText(templateContent)) {
            throw TemplateNotFoundException.of();
        }
    }

    @Override
    public boolean supports(ConvertTypeEnum convertType, boolean isSecurity) {
        return convertType == ConvertTypeEnum.HTML && isSecurity == false;
    }
}
