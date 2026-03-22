package com.ums.schedule.template.application.dto;

import com.ums.schedule.template.domain.code.EmailTemplateSectionEnum;
import com.ums.schedule.template.domain.email.EmailContent;

public record EmailContentDto(
        EmailContent header,
        EmailContent body,
        EmailContent footer
) {

}
