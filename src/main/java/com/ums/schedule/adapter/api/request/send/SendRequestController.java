package com.ums.schedule.adapter.api.request.send;

import com.ums.schedule.application.ums.common.request.SendRequestRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/send-requests")
public class SendRequestController {
    private final SendRequestRequestService requestService;
    @PatchMapping("/{requestId}/requests/{uploadId}")
    public ResponseEntity request(@PathVariable(value = "requestId") Long requestId, @PathVariable(value = "uploadId") String uploadId) {
        requestService.request(requestId, uploadId);
        return ResponseEntity.ok().build();
    }
}
