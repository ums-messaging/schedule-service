package com.ums.schedule.application.sendrequest.message.email.command;

import com.ums.schedule.application.template.email.query.model.EmailTemplateDetailResult;
import com.ums.schedule.application.template.email.command.EmailTemplateContentCommand;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;

import java.util.Map;

public record EmailSendMessageCreateCommand(
        String title,
        Map<EmailTemplateSectionEnum, EmailTemplateContentCommand> templateMap
) {
   public static EmailSendMessageCreateCommand of(EmailTemplateDetailResult template, Map<EmailTemplateSectionEnum, EmailTemplateContentCommand> templateMap) {
      return new EmailSendMessageCreateCommand(
              template.msgTitle(),
              templateMap
      );
   }
}
