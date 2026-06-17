package com.ums.schedule.application.sendrequest.target.email;

import com.ums.schedule.application.sendrequest.message.email.processor.EmailTargetMessageProcessor;
import com.ums.schedule.application.sendrequest.message.email.result.EmailTargetMessageResult;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;
import com.ums.schedule.domain.sendrequest.template.email.EmailTemplate;
import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import com.ums.schedule.domain.sendrequest.target.exeption.SendTargetException;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailSendTargetGenerator {
    private final Map<ConvertTypeEnum, EmailTargetMessageProcessor> resourceMap;

    public SendTarget generate(TargetUploadReport targetUploadReport, EmailTemplate template, List<EmailAttachment> messages, TargetMessageData targetDto)  {
        try {
            SendTarget target = SendTarget.of(targetUploadReport, targetDto, template);
            List<EmailTargetMessageResult> resources = messages.stream()
                    .map(message -> {
                        EmailTargetMessageProcessor processor = resourceMap.get(message.getConvertType());
                        return processor.process(message, target);
                    })
                    .toList();
            target.assignResources(resources);
            return target;
        } catch (SendTargetException e) {
            return SendTarget.failureOf(targetDto, e.getMessage());
        }
    }
}
