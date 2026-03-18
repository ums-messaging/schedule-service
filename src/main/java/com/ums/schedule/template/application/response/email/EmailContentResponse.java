package com.ums.schedule.template.application.response.email;


public record EmailContentResponse(
        String emailTemplateId,
        String versionId,
        String format,
        String section,
        String content
) {

}
