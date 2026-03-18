package com.ums.schedule.message.application.assembler;

import com.ums.schedule.message.application.command.EmailMessageCommand;
import com.ums.schedule.message.domain.email.EmailSendMessage;
import com.ums.schedule.template.application.assembler.TemplateAssembler;
import com.ums.schedule.template.application.response.TemplateResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateResponse;
import com.ums.schedule.template.domain.email.EmailTemplate;
import com.ums.schedule.template.infrastructure.TemplateClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailMessageAssembler {
    private final TemplateClient templateClient;
    private final TemplateAssembler assembler;

    public EmailSendMessage create(EmailMessageCommand command) {
        EmailTemplateResponse response = templateClient.getTemplate(command.templateId());
        EmailTemplate template = assembler.toTemplateMap(response);
        return EmailSendMessage.of(template);
    }
}
