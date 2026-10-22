package com.ums.schedule.email.adaptor.in.rest.message;

import com.ums.schedule.common.code.api.ApiResponseCode;
import com.ums.schedule.email.adaptor.in.rest.message.response.SendMessageCreateResponse;
import com.ums.schedule.email.adaptor.in.rest.message.request.MessageCreateRequest;
import com.ums.schedule.email.application.message.EmailMessageCreateService;
import com.ums.schedule.orchestrator.adaptor.in.rest.common.ApiResponse;
import com.ums.schedule.email.application.message.command.SendMessageCreateSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/messages")
public class MessageCreateController {
    private final EmailMessageCreateService createService;

    @PostMapping
    public ResponseEntity<ApiResponse<SendMessageCreateResponse>> create(@RequestHeader("X-CUSTOMER-ID") final String customerId,
                                                                         @RequestBody final MessageCreateRequest request) {
        SendMessageCreateSummary summary = createService.create(customerId, request);
        SendMessageCreateResponse response = SendMessageCreateResponse.of(summary);
        return ResponseEntity.ok(ApiResponse.of(ApiResponseCode.SUCCESS, response));
    }
}
