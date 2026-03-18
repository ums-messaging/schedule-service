package com.ums.schedule.template.application.response.email;

import java.util.List;

public record EmailTemplateDetailResponse(
        String emailTemplateId,
        String version,
        String baseDir,
        String imageDir,
        List<EmailContentResponse> contents
) {
}
