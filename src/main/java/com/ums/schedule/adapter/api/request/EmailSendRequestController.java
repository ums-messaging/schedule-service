package com.ums.schedule.adapter.api.request;

import com.ums.schedule.adapter.api.request.response.EmailSendRequestCreateResponse;
import com.ums.schedule.adapter.api.request.request.EmailSendCreateRequest;
import com.ums.schedule.application.sendrequest.EmailSendRequestCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/request/email")
public class EmailSendRequestController {
    private final EmailSendRequestCreateService process;

    @PostMapping
    public ResponseEntity<EmailSendRequestCreateResponse> create(String customerId, @RequestBody EmailSendCreateRequest request) {
        EmailSendRequestCreateResponse result = process.create(customerId, request);
        return ResponseEntity.ok(result);
    }
}
