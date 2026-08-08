package com.ums.schedule.application.ums.common.template.loader;

import com.ums.schedule.application.ums.common.template.loader.model.ChannelTemplate;
import com.ums.schedule.domain.message.email.EmailSendMessage;

import java.util.UUID;

public interface TemplateLoader {
    ChannelTemplate loadTemplate(EmailSendMessage messageId);
}
