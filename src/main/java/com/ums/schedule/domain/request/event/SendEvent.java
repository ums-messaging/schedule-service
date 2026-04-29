package com.ums.schedule.domain.request.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ums.schedule.code.send.SendRequestEventTypeEnum;
import com.ums.schedule.code.send.SendRequestStatusEnum;

import java.io.IOException;

public interface SendEvent {
    @JsonIgnore
    SendRequestEventTypeEnum getEventType();
    @JsonIgnore
    SendRequestStatusEnum getRequestStatus();

    default String toJson(ObjectMapper mapper) throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }
}
