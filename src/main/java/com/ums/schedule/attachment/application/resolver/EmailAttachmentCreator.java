package com.ums.schedule.attachment.application.resolver;

import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.application.command.EmailMessageCommand;
import com.ums.schedule.send.application.model.dto.SendTargetDto;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;

public class EmailAttachmentCreator implements AttachmentCreator {
    @Override
    public Attachment createAttachment(EmailMessageCommand command, EmailContentResponse content, SendTargetDto targetDto) {
        Attachment attachment = Attachment.of(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.NONE));
        attachment.defineFileMetadata(content);
        attachment.defineAttachmentPolicy(targetDto.parse(content.attachmentName()), targetDto.parse(content.downloadName()));
        return attachment;
    }
}
