package com.ums.schedule.application.message.email.model;

import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateDetailResult;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;

import java.util.Map;

public record EmailMessageCreateCommand(
        String title,
        Map<EmailTemplateSectionEnum, String> templateMap
) {
   public static EmailMessageCreateCommand of(EmailTemplateDetailResult template,
                                              Map<EmailTemplateSectionEnum, String> templateMap) {
      return new EmailMessageCreateCommand(
              template.msgTitle(),
              templateMap
      );
   }

//   public Template getHeaderTemplate() {
//      if(templateMap.containsKey(EmailTemplateSectionEnum.HEADER)) {
//         return templateMap.get(EmailTemplateSectionEnum.HEADER).template();
//      }
//      return null;
//   }
//
//   public Template getFooterTemplate() {
//      if(templateMap.containsKey(EmailTemplateSectionEnum.FOOTER)) {
//         return templateMap.get(EmailTemplateSectionEnum.FOOTER).template();
//      }
//      return null;
//   }
//
//   public Template getBodyTemplate() {
//      if(templateMap.containsKey(EmailTemplateSectionEnum.BODY)) {
//         return templateMap.get(EmailTemplateSectionEnum.BODY).template();
//      }
//      return null;
//   }
}
