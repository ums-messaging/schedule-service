package com.ums.schedule.email.application.message.handler;

import com.ums.schedule.common.code.email.security.EncryptionTypeEnum;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.email.application.send.mime.model.MimeAttachFile;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HtmlConvertHandler implements EmailConvertHandler {
    private final Configuration configuration;

    @Override
    public boolean supports(ConvertType convertType, EncryptionTypeEnum encryptionType) {
        return convertType == ConvertType.HTML;
    }

    public Path handle(MimeAttachFile attachFile, Map<String, Object> targetData) throws IOException {
        Path resultPath = Files.createTempFile(
                "email-message-", ".html"
        );
        try (
                Reader reader = Files.newBufferedReader(
                        attachFile.file(), StandardCharsets.UTF_8
                );
                Writer writer = Files.newBufferedWriter(
                        resultPath, StandardCharsets.UTF_8
                )
        ) {
            Template template = new Template(
                    "email-message",
                    reader,
                    configuration
            );

            template.process(targetData, writer);

        } catch (IOException | TemplateException | RuntimeException e) {
            Files.deleteIfExists(resultPath);
//            throw e;
        }
        return resultPath;
    }
}
