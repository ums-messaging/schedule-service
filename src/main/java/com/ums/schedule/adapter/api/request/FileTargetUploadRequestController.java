package com.ums.schedule.adapter.api.request;

import com.ums.schedule.application.target.processor.FileTargetUploadProcessor;
import com.ums.schedule.application.target.processor.model.FileTargetUploadRequestResult;
import com.ums.schedule.common.code.api.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/targets/uploads")
public class FileTargetUploadRequestController {
    private final FileTargetUploadProcessor processor;

    @PostMapping(value = "/{uploadId}/requests")
    public ResponseEntity<ApiResponse<FileTargetUploadRequestResult>> request(@PathVariable(value = "uploadId") String uploadId) {
        FileTargetUploadRequestResult result = processor.request(uploadId);
        FileTargetUploadRequestResponse response = FileTargetUploadRequestResponse.of(null, result);

        return ResponseEntity.ok(ApiResponse.of(ApiResponseCode.SUCCESS, response));
    }
}
