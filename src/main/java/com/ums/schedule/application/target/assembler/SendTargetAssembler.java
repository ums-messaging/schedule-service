package com.ums.schedule.application.target.assembler;

import com.ums.schedule.application.channel.email.converter.EmailBodyConverter;
import com.ums.schedule.application.target.dto.SendTargetDto;
import com.ums.schedule.domain.target.exeption.SendTargetException;
import com.ums.schedule.domain.channel.email.message.EmailMessage;
import com.ums.schedule.domain.target.SendTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendTargetAssembler {
    private final EmailBodyConverter converter;

    public SendTarget assemble(EmailMessage job, SendTargetDto targetDto) {
        SendTarget target ;
        try {
            target = SendTarget.of(targetDto, job.template());
            converter.createAttachment(job.body(), job.template().getAttachments(), target);
            target.toReady();
            return target;
        } catch (SendTargetException e) {
            throw e;
        }
    }
}
