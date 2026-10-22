package com.ums.schedule.email.adaptor.in.rest.template;

import com.ums.schedule.common.code.api.ApiResponseCode;
import com.ums.schedule.email.adaptor.in.rest.message.request.MessageCreateRequest;
import com.ums.schedule.email.adaptor.in.rest.message.response.SendMessageCreateResponse;
import com.ums.schedule.email.adaptor.in.rest.template.request.EmailTemplateCreateRequest;
import com.ums.schedule.email.adaptor.in.rest.template.response.TemplateCreateResponse;
import com.ums.schedule.email.application.message.command.SendMessageCreateSummary;
import com.ums.schedule.email.application.template.EmailTemplateCreateService;
import com.ums.schedule.email.domain.template.EmailTemplate;
import com.ums.schedule.orchestrator.adaptor.in.rest.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/templates")
public class TemplateCreateController {
    private final EmailTemplateCreateService createService;
    @PostMapping
    public ResponseEntity<ApiResponse<TemplateCreateResponse>> create(@RequestHeader("X-CUSTOMER-ID") final String customerId,
                                                                      @RequestBody final EmailTemplateCreateRequest request) {
        EmailTemplate template = createService.create(customerId, request);
        TemplateCreateResponse response = TemplateCreateResponse.of(template);
        return ResponseEntity.ok(ApiResponse.of(ApiResponseCode.SUCCESS, response));
    }
}
