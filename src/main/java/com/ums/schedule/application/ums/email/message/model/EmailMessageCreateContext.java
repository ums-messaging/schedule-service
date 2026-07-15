package com.ums.schedule.application.ums.email.message.model;

import com.ums.schedule.application.ums.email.convert.EmailConvertPolicy;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateDetailResult;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.common.code.email.EmailMessageSection;

public record EmailMessageCreateContext(
        SendMessage sendMessage,
        String title,
        String headerKey,
        String bodyKey,
        String footerKey
) {
   public static EmailMessageCreateContext of(SendMessage sendMessage, EmailTemplateDetailResult template, EmailConvertPolicy policy) {
      return new EmailMessageCreateContext(
              sendMessage,
              template.msgTitle(),
              template.getHeaderFooter().get(EmailMessageSection.HEADER).fileKey(),
              policy.bodyKey(),
              template.getHeaderFooter().get(EmailMessageSection.FOOTER).fileKey()
      );
   }
}
