package com.ums.schedule.application.sendrequest.target.assembler;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.sendrequest.target.email.EmailSendTargetGenerator;
import com.ums.schedule.application.ums.email.template.EmailTemplateLoader;
import com.ums.schedule.domain.sendrequest.template.email.EmailTemplate;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailSendTargetAssembler implements SendTargetAssembler {
    private final EmailSendTargetGenerator generator;
    private final EmailTemplateLoader templateLoader;

    @Override
    public List<SendTarget> assemble(String messageId, TargetUploadReport targetUpload, List<TargetMessageData> targetList) {
        EmailTemplate template = templateLoader.loadTemplate(messageId);
        return targetList.stream()
                .map(targetData -> generator.generate(targetUpload, template, targetData))
                .toList();
    }
}
