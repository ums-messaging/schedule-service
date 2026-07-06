package com.ums.schedule.application.sendrequest.message.email.processor;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.sendrequest.message.email.pipeline.SecurityEncryptionPipeline;
import com.ums.schedule.application.sendrequest.message.email.result.EmailTargetMessageResult;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class EmailTargetMessageConvertProcessor implements EmailTargetMessageProcessor {
    private final String uploadPrefix = "";
    private final SecurityEncryptionPipeline handler;
    private final AwsS3Repository repository;

    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return !ConvertTypeEnum.valueOf(mapperValue.code()).equals(ConvertTypeEnum.NONE);
    }

    @Override
    public EmailTargetMessageResult process(EmailAttachment message, SendTarget target) {
        File file = null;
        try {
            String fileKey = uploadPrefix+File.separator+ EmailTemplateSectionEnum.BODY.value();
            String uploadKey = target.parse(message.getFileKey());
            InputStream inputStream = repository.getFileContent(fileKey);
            String templateContent =
                    new String(
                            inputStream.readAllBytes(),
                            StandardCharsets.UTF_8
                    );
            Template template = new Template("template", new StringReader(templateContent));
            StringWriter writer = new StringWriter();
            template.process(target.getDataParam(), writer);
            file = handler.handle(message, writer.toString(), target);
            String objectKey = repository.upload(file, uploadKey);
            return EmailTargetMessageResult.of(objectKey, message.getAttachmentPolicy(), target);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}
