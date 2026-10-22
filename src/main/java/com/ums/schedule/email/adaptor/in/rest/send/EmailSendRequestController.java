package com.ums.schedule.email.adaptor.in.rest.send;

import com.ums.schedule.common.code.api.ApiResponseCode;
import com.ums.schedule.common.mapper.EmailSendRequestResponse;
import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.email.adaptor.in.rest.send.request.EmailSendRequestRequest;
import com.ums.schedule.email.application.requests.EmailSendRequestCreateService;
import com.ums.schedule.email.application.requests.result.SendRequestSaveResult;
import com.ums.schedule.orchestrator.adaptor.in.rest.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSendRequestController {
    private final EmailSendRequestCreateService createService;

    public ApiResponse<String> create(String json) {
        EmailSendRequestRequest request = JsonUtil.toObject(json, EmailSendRequestRequest.class);
        log.info("request = {}", request);
        SendRequestSaveResult result = createService.request(request);
        EmailSendRequestResponse response = EmailSendRequestResponse.of(result);
        String toJson = JsonUtil.toJson(response);
        return ApiResponse.of(ApiResponseCode.SUCCESS, toJson);
    }
}
