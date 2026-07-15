package com.ums.schedule.adapter.api.request;

import com.ums.schedule.adapter.api.request.email.EmailSendCreateRequest;
import com.ums.schedule.application.ums.email.request.EmailSendRequestCreateService;
import com.ums.schedule.application.ums.common.request.model.SendRequestCreateResult;
import com.ums.schedule.common.code.target_upload.TargetUploadTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/request/email")
public class EmailSendRequestController {
    private final EmailSendRequestCreateService process;

    @PostMapping
    public ResponseEntity<SendRequestCreateResult> create(String customerId, @RequestBody EmailSendCreateRequest request) {
        SendRequestCreateResult result = process.create(customerId, TargetUploadTypeEnum.FILE, request);
        return ResponseEntity.ok(result);
    }
}
