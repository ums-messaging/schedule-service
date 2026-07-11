package com.ums.schedule.application.sendrequest.target.email;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.exception.ApplicationException;
import com.ums.schedule.application.message.email.EmailResourceCommand;
import com.ums.schedule.application.message.email.model.AttachmentPipelineCommand;
import com.ums.schedule.application.exception.EmailMessageConvertException;
import com.ums.schedule.application.ums.email.convert.handler.AttachmentConverter;
import com.ums.schedule.application.message.email.result.TemplateConversionResult;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.domain.sendrequest.template.email.EmailTemplate;
import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailSendTargetGenerator {
    private final AwsS3Repository fileRepository;
    private final List<AttachmentConverter> processors;

    public SendTarget generate(TargetUploadReport targetUploadReport, EmailTemplate template, TargetMessageData targetDto)  {
        try {
            SendTarget target = SendTarget.of(targetUploadReport, targetDto, template);
            List<EmailResourceCommand> resources = template.getAttachmentList().stream()
                    .map(attachment -> generateAttachment(attachment, target))
                    .toList();
            target.generateAttachments(resources);
            return target;
        } catch (ApplicationException e) {
            return SendTarget.failureOf(targetDto, e.getMessage());
        }
    }

    private EmailResourceCommand generateAttachment(EmailAttachment attachment, SendTarget target) {
        AttachmentPipelineCommand command = AttachmentPipelineCommand.of(attachment, target);;
        boolean isSecurity = attachment.hasSecurityPolicy();

        AttachmentConverter pipeline = processors.stream()
                .filter(p -> p.supports(attachment.getConvertType(), isSecurity))
                .findFirst()
                .orElseGet(()->null);

        if(pipeline != null) {
            convertAndUpload(command, pipeline);
        }
        return EmailResourceCommand.of(command);
    }

    private TemplateConversionResult convertAndUpload(AttachmentPipelineCommand command, AttachmentConverter p) {
        try {
            TemplateConversionResult result = p.handle(command);
            fileRepository.upload(result.tempFile(), command.objectKey());
            return result;
        } catch (IOException e) {
            throw EmailMessageConvertException.of(e);
        }
    }
}
