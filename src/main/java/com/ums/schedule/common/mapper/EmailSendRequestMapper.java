package com.ums.schedule.common.mapper;

import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.email.application.requests.EmailSendRequestCreateService;
import com.ums.schedule.email.application.requests.command.SendRequestCommand;
import com.ums.schedule.email.application.requests.result.SendRequestSaveResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailSendRequestMapper {
    private final EmailSendRequestCreateService createService;

}
