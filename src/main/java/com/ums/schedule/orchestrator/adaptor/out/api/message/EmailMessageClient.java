package com.ums.schedule.orchestrator.adaptor.out.api.message;

import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.email.adaptor.in.rest.send.EmailSendRequestController;
import com.ums.schedule.orchestrator.adaptor.in.rest.common.ApiResponse;
import com.ums.schedule.orchestrator.adaptor.out.api.message.request.ChannelSendRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailMessageClient implements ChannelMessageClient {
    private final EmailSendRequestController controller;

    public Long createChannelRequest(ChannelSendRequest request) {
        String json = JsonUtil.toJson(request);
        log.info("json = {}", json);
        ApiResponse<String> response = controller.create(json);
        // 응답 값이 200이 아니면 예외 발생시키기
        return request.requestId();
    }

    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return ChannelType.EMAIL == ChannelType.valueOf(mapperValue.code());
    }

    private record EmailSendRequestResult (Long requestId) {

    }

}
