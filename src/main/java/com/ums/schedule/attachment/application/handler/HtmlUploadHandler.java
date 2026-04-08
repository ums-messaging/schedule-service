package com.ums.schedule.attachment.application.handler;

import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.common.code.EnumMapperFactory;
import com.ums.schedule.send.application.model.dto.SendTargetDto;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import static com.ums.schedule.template.domain.code.ConvertTypeEnum.NONE;

@Component
@RequiredArgsConstructor
public class HtmlUploadHandler implements EmailBodyHandler {
    private final EnumMapperFactory factory;

    // html 업로드
    @Override
    public File handle(Attachment attachment, Template template, SendTargetDto targetDto) throws IOException {
        if(attachment.getConvertType() != NONE) {
            File file = File.createTempFile("", ".html");

            try (Writer writer = new FileWriter(file)) {
                template.process(targetDto.extractMessageVariable(), writer);
            } catch (TemplateException e) {
                e.printStackTrace();
            }
            return file;
        }
        return null;
    }

    private String parseName(String name, Map<String, Object> extractMessageVariable) {
        return name.replace("", "");
    }
}
