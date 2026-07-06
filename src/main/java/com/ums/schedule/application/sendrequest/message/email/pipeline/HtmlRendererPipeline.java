package com.ums.schedule.application.sendrequest.message.email.pipeline;

import com.ums.schedule.application.sendrequest.template.loader.TemplateFileLoader;
import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@RequiredArgsConstructor
public class HtmlRendererPipeline implements MessageGenerationPipeline {
    private final Configuration configuration;
    private final TemplateFileLoader templateLoader;
    // html 업로드
    @Override
    public File handle(EmailAttachment message, String html, SendTarget target) throws IOException {
        if(message.getConvertType() != ConvertTypeEnum.NONE) {
            File file = File.createTempFile(target.getId().toString(), message.getFileKey());


           templateLoader.loadAndCompileTemplate(message.getFileKey(), target.getDataParam(), file);
        }
        return File.createTempFile(target.getId().toString(), ConvertTypeEnum.HTML.value());
    }
}
