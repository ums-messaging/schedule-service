package com.ums.schedule.adapter.api.template.email;

import com.ums.schedule.adapter.api.template.TemplateResponse;

public record EmailTemplateResponse(
        TemplateResponse template,
        EmailTemplateDetailResponse emailTemplate
) {

}
