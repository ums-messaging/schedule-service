package com.ums.schedule.attachment.application.handler;

import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.application.command.EmailMessageCommand;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;
import com.ums.schedule.template.domain.code.EmailTemplateSectionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.ums.schedule.template.domain.code.ConvertTypeEnum.NONE;
import static com.ums.schedule.template.domain.code.EmailTemplateSectionEnum.BODY;

@Component
@RequiredArgsConstructor
public class SectionTypeHandler implements AttachmentHandler {
    private final @Qualifier("securityTypeHandler") AttachmentHandler handler;

    @Override
    public Attachment handle(EmailMessageCommand command, EmailContentResponse body) {
        if(body.section().equals(BODY.value())) {
            Attachment attachment = handler.handle(command, body);
            if(attachment.getConvertType() == NONE) {
                return null;
            }
            return attachment;
        }
        return Attachment.of(EnumMapperValue.fromEnumMapperType(NONE));
    }
}
