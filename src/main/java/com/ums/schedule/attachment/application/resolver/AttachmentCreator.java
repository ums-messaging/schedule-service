package com.ums.schedule.attachment.application.resolver;

import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.message.application.command.EmailMessageCommand;
import com.ums.schedule.send.application.model.dto.SendTargetDto;
import com.ums.schedule.template.application.response.email.EmailContentResponse;

public interface AttachmentCreator {
    Attachment createAttachment(EmailMessageCommand command, EmailContentResponse content, SendTargetDto targetDto);
}
