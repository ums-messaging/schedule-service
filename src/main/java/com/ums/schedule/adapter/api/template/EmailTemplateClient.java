package com.ums.schedule.adapter.api.template;

import com.ums.schedule.application.template.email.query.model.EmailTemplateResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "emailTemplate",
        url = "http://localhost:8080"
)
public interface EmailTemplateClient {
    @GetMapping("/v1/template/email/{templateId}")
    EmailTemplateResult getTemplate(@PathVariable("templateId") String templateId);
}
