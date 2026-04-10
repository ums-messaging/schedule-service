package com.ums.schedule.message.application.assembler;

import com.ums.schedule.attachment.application.model.AttachmentDto;
import com.ums.schedule.attachment.application.converter.EmailBodyConverter;
import com.ums.schedule.common.code.EnumMapperFactory;
import com.ums.schedule.send.domain.request.EmailBody;
import com.ums.schedule.send.domain.target.EmailSendTarget;
import com.ums.schedule.template.domain.email.EmailTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailMessageAssembler {
    private final EnumMapperFactory enumMapperFactory;
    private final Map<String, EmailBodyConverter> converter;

    public TargetMessageDto createMessage(EmailBody body, EmailTemplate template, List<AttachmentDto> attachmentDtos, EmailSendTarget target) {
        EmailBodyConverter converter = this.converter.get(body.getConvertType().value());

        return null;
    }


    public static class TargetMessageDto {

    }
}
