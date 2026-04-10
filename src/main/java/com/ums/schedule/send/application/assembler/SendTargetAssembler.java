package com.ums.schedule.send.application.assembler;

import com.ums.schedule.attachment.application.converter.EmailBodyConverter;
import com.ums.schedule.send.EmailSendJob;
import com.ums.schedule.send.application.model.dto.SendTargetDto;
import com.ums.schedule.send.domain.request.EmailBody;
import com.ums.schedule.send.domain.request.upload.TargetUpload;
import com.ums.schedule.send.domain.target.EmailSendTarget;
import com.ums.schedule.send.domain.target.exception.SendTargetException;
import com.ums.schedule.template.domain.email.EmailTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SendTargetAssembler {
    private final EmailBodyConverter converter;

    public EmailSendTarget assemble(EmailSendJob job, SendTargetDto targetDto) {
        EmailSendTarget target ;
        try {
            target = EmailSendTarget.of(targetDto, job.template());
            converter.createAttachment(job.body(), job.template().getAttachments(), target);
            target.toReady();
            return target;
        } catch (SendTargetException e) {
            throw e;
        }
    }
}
