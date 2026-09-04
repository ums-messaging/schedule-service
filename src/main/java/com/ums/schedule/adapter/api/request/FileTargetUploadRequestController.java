package com.ums.schedule.adapter.api.request;

import com.ums.schedule.application.target.processor.FileTargetUploadProcessor;
import com.ums.schedule.application.target.processor.model.FileTargetUploadRequestResult;
import com.ums.schedule.common.code.api.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/send-requests")
public class FileTargetUploadRequestController {
    private final FileTargetUploadProcessor processor;

    @PostMapping(value = "/{requestId}/targets/uploads/{uploadId}/requests")
    public ResponseEntity<ApiResponse<FileTargetUploadRequestResult>> request(@PathVariable(value = "requestId") Long requestId, @PathVariable(value = "uploadId") String uploadId) {
        FileTargetUploadRequestResult result = processor.request(uploadId);
        FileTargetUploadRequestResponse response = FileTargetUploadRequestResponse.of(requestId, result);

        return ResponseEntity.ok(ApiResponse.of(ApiResponseCode.SUCCESS, response));
    }
}
