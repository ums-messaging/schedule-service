package com.ums.schedule.orchestrator.adaptor.in.rest.upload;

import com.ums.schedule.common.code.api.ApiResponseCode;
import com.ums.schedule.orchestrator.adaptor.in.rest.common.ApiResponse;
import com.ums.schedule.orchestrator.adaptor.in.rest.upload.request.SendRequestUploadCreateRequest;
import com.ums.schedule.orchestrator.adaptor.in.rest.upload.request.SendRequestUploadRequest;
import com.ums.schedule.orchestrator.adaptor.in.rest.upload.response.SendRequestUploadResponse;
import com.ums.schedule.orchestrator.application.upload.SendRequestUploadRequestService;
import com.ums.schedule.orchestrator.application.upload.SendRequestUploadService;
import com.ums.schedule.orchestrator.application.upload.result.SendRequestUploadRequestResult;
import com.ums.schedule.orchestrator.application.upload.result.SendRequestUploadResult;
import com.ums.schedule.orchestrator.domain.upload.SendRequestUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/requests/upload")
public class SendRequestUploadController {
    private final SendRequestUploadService createService;
    private final SendRequestUploadRequestService requestService;

    @PostMapping
    public ResponseEntity<ApiResponse<SendRequestUploadResponse>> create(@RequestHeader("X-CUSTOMER-ID") final String customerId,
                                                                         @RequestBody final SendRequestUploadCreateRequest request) {
        SendRequestUploadResult result = createService.create(customerId, request);
        SendRequestUploadResponse response = SendRequestUploadResponse.of(result);
        return ResponseEntity.ok(ApiResponse.of(ApiResponseCode.SUCCESS, response));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") Long id) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/file/{findKey}")
    public ResponseEntity<ApiResponse> fileRequest(@RequestHeader("X-CUSTOMER-ID") final String customerId, @PathVariable("findKey") final String findKey) {
        SendRequestUploadRequestResult result = requestService.request(customerId, findKey);
        return ResponseEntity.ok(ApiResponse.of(ApiResponseCode.SUCCESS, result));
    }

    @PostMapping("/{fileKey}")
    public ResponseEntity<ApiResponse> apiRequest(@RequestHeader("X-CUSTOMER-ID") final String customerId,
                                                  @PathVariable("findKey") final String findKey,
                                                  @RequestBody List<SendRequestUploadRequest> requests
                                                  ) {
        return ResponseEntity.ok(ApiResponse.of(ApiResponseCode.SUCCESS, null));
    }
}
