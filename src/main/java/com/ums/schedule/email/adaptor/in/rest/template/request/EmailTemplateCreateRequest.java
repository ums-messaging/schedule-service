package com.ums.schedule.email.adaptor.in.rest.template.request;

public record EmailTemplateCreateRequest(
        String templateKey,
        String imageDir
) {
}
