package com.ums.schedule.application.sendrequest.target.email;

import com.ums.schedule.application.sendrequest.target.assembler.SendTargetAssembler;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.template.email.EmailTemplate;
import com.ums.schedule.domain.sendrequest.message.email.EmailSendMessage;
import com.ums.schedule.domain.sendrequest.message.email.EmailSendMessageJpaRepository;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import com.ums.schedule.domain.sendrequest.target.code.SendTargetStatusEnum;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.util.UUIDConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmailSendTargetAssembler implements SendTargetAssembler<EmailTemplate> {
    private final EmailSendTargetGenerator generator;
    private final EmailSendMessageJpaRepository messageRepository;
    private final Configuration configuration;

    @Override
    public Map<SendTargetStatusEnum, List<SendTarget>> assemble(String messageId, TargetUploadReport targetUpload, List<TargetMessageData> targetList)  {
        UUID uuid = UUIDConverter.getUUID(messageId);
        EmailSendMessage findMessage = messageRepository.findById(uuid).orElseThrow();
        List<EmailAttachment> messages = new ArrayList<>();
        try {
            Template html = new Template("email_body", findMessage.getBodyTemplate(), configuration);
            EmailTemplate template = EmailTemplate.of(findMessage.getSubject(), html, messages);
            return targetList.stream()
                    .map(targetData ->
                            generator.generate(targetUpload, template, messages,  targetData)
                    ).collect(Collectors.groupingBy(SendTarget::getStatus));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
