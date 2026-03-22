package com.ums.schedule.template.application.response.email;

import com.ums.schedule.template.application.response.TemplateResponse;

import java.util.List;

public record EmailTemplateResponse(
        TemplateResponse template,
        EmailTemplateDetailResponse emailTemplate
) {

}
