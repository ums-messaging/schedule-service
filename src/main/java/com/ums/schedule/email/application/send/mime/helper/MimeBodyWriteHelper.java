package com.ums.schedule.email.application.send.mime.helper;

import com.ums.schedule.email.application.send.mime.model.MimeMessage;
import com.ums.schedule.email.application.send.mime.writer.MimeWriter;
import com.ums.schedule.email.domain.target.SendTargetRequest;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Slf4j
@Component(value = "mimeHtmlWriter")
@RequiredArgsConstructor
public class MimeBodyWriteHelper implements MimeWriteHelper {
    private final Configuration configuration;

    @Override
    public Writer write(MimeWriter writer, SendTargetRequest target) throws IOException, TemplateException {
        MimeMessage mimeMessage = writer.mimeMessage();
        Path templateFile = mimeMessage.body();
        try (InputStream is = Files.newInputStream(templateFile)) {
            Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            Map<String, Object> targetData = target.getMessageVariable();
            Template template = new Template(String.valueOf(targetData.get("id")), reader, configuration);
            mimeMessage.setBody(writer, template, target.getMessageVariable());
        }
        return writer.writer();
    }
}
