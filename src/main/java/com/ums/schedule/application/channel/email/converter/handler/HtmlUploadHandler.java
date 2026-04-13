package com.ums.schedule.application.channel.email.converter.handler;

import com.ums.schedule.application.channel.email.dto.AttachmentDto;
import com.ums.schedule.code.EnumMapperFactory;
import com.ums.schedule.domain.channel.email.message.EmailBody;
import com.ums.schedule.domain.target.SendTarget;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

@Component
@RequiredArgsConstructor
public class HtmlUploadHandler implements EmailBodyConvertHandler {
    private final Configuration configuration;
    private final EnumMapperFactory factory;

    // html 업로드
    @Override
    public File handle(AttachmentDto attachment, EmailBody body, SendTarget target) throws IOException {
        if(attachment.isConvert()) {
            File file = File.createTempFile("", ".html");

            try (Writer writer = new FileWriter(file)) {
                body.getTemplate().process(target.getMessageVariable(), writer);
            } catch (TemplateException e) {
                e.printStackTrace();
            }
            return file;
        }
        return null;
    }
}
