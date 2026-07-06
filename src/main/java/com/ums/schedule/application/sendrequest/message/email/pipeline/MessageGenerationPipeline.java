package com.ums.schedule.application.sendrequest.message.email.pipeline;

import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.target.SendTarget;

import java.io.File;
import java.io.IOException;

public interface MessageGenerationPipeline {
    File handle(EmailAttachment message, String html, SendTarget target) throws IOException;
}
