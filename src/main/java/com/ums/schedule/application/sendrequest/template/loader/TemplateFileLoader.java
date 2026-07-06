package com.ums.schedule.application.sendrequest.template.loader;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.template.email.code.TemplateContentFormatEnum;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class TemplateFileLoader implements TemplateLoader {
    private final AwsS3Repository fileRepository;
    private final Configuration configuration;

    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return TemplateContentFormatEnum.valueOf(mapperValue.value()) == TemplateContentFormatEnum.HTML;
    }

    public void loadAndCompileTemplate(String fileKey, Map<String, Object> messageVariable, File file) {
        try (Writer writer = new FileWriter(file)) {
            loadAndCompileTemplate(fileKey, messageVariable, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAndCompileTemplate(String fileKey, Map<String, Object> messageVariable) {
        loadAndCompileTemplate(fileKey, messageVariable, new StringWriter());
    }

    private void loadAndCompileTemplate(String fileKey, Map<String, Object> messageVariable, Writer writer) {
        InputStream is = fileRepository.getFileContent(fileKey);
        try(writer) {
            String fileContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Template template = new Template("email_body", fileContent, configuration);
            template.process(messageVariable, writer);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String loadTemplate(String fileKey)  {
        InputStream inputStream = fileRepository.getFileContent(fileKey);
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        return reader.toString();
    }
}
