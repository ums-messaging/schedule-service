package com.ums.schedule.application.channel.email.converter.handler;

import com.ums.schedule.application.channel.email.dto.AttachmentDto;
import com.ums.schedule.domain.channel.email.message.EmailBody;
import com.ums.schedule.domain.target.SendTarget;

import java.io.File;
import java.io.IOException;

public interface EmailBodyConvertHandler {
    File handle(AttachmentDto attachment, EmailBody body, SendTarget target) throws IOException;
}
