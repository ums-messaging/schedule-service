package com.ums.schedule.orchestrator.adaptor.in.rest.requests;


import com.ums.schedule.common.code.common.CommonCode;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.orchestrator.adaptor.in.config.SendRequestConfigProvider;
import com.ums.schedule.orchestrator.adaptor.in.rest.common.ApiResponse;
import com.ums.schedule.common.code.api.ApiResponseCode;
import com.ums.schedule.orchestrator.adaptor.in.rest.requests.request.SendRequestCreateRequest;
import com.ums.schedule.orchestrator.adaptor.in.rest.requests.response.SendRequestCreateResponse;
import com.ums.schedule.orchestrator.application.requests.command.SendRequestCreateCommand;
import com.ums.schedule.orchestrator.application.requests.model.SendRequestCreateResult;
import com.ums.schedule.orchestrator.application.requests.SendRequestCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/send/requests")
public class SendRequestController {
    private final SendRequestConfigProvider provider;
    private final EnumMapperFactory factory;
    private final SendRequestCreateService createService;

    @PostMapping
    public ResponseEntity<ApiResponse<SendRequestCreateResponse>> request(@RequestHeader("X-CUSTOMER-ID") final String customerId,
                                                                          @RequestBody final SendRequestCreateRequest request) {
        EnumMapperValue channelType = factory.findEnumMapperValue(CommonCode.CHANNEL_TYPE, request.channel());
        SendRequestCreateCommand command = request.toCommand(channelType, customerId, provider.loadConfig());
        SendRequestCreateResult result = createService.create(command);
        SendRequestCreateResponse response = SendRequestCreateResponse.of(result);
        return ResponseEntity.ok(ApiResponse.of(ApiResponseCode.SUCCESS, response));
    }
}
