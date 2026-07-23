package com.ums.schedule.adapter.api.request.email;

import com.ums.schedule.adapter.api.request.ApiResponse;
import com.ums.schedule.adapter.api.request.email.request.EmailSendCreateRequest;
import com.ums.schedule.adapter.api.request.email.response.FileEmailSendRequestResponse;
import com.ums.schedule.adapter.api.request.email.validator.EmailSendRequestValidator;
import com.ums.schedule.application.ums.email.request.EmailSendRequestCreateService;
import com.ums.schedule.application.ums.email.request.model.EmailSendRequestCreateSummary;
import com.ums.schedule.common.code.api.ApiResponseCode;
import com.ums.schedule.common.code.target_upload.TargetUploadType;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/send-requests/email/upload-url")
public class FileEmailSendRequestController {
    private final EmailSendRequestValidator validator;
    private final EmailSendRequestCreateService process;

    @InitBinder
    void init(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FileEmailSendRequestResponse>> create(@RequestHeader("X-CUSTOMER-ID") final String customerId,
                                                                            @Valid @RequestBody final EmailSendCreateRequest request) {
        EmailSendRequestCreateSummary summary = process.create(customerId, TargetUploadType.FILE, request);
        FileEmailSendRequestResponse toResponse = FileEmailSendRequestResponse.of(summary);
        ApiResponse<FileEmailSendRequestResponse> response = ApiResponse.of(ApiResponseCode.CREATED, toResponse);
        return ResponseEntity.ok(response);
    }
}
