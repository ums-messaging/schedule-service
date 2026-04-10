package com.ums.schedule.send.application.service;

import com.ums.schedule.send.application.model.command.SendRequestCommand;
import com.ums.schedule.send.domain.request.SendRequest;

public interface SendRequestFactory {
    SendRequest createSendRequest(SendRequestCommand command);

}
