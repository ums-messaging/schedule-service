package com.ums.schedule.adapter.api.request;

public record SendRequestCreateRequest(
        Long scheduleId,
        String customerRequestKey,
        String uploadFormat,
        String senderKey,
        String templateKey,
        String messageType,
        Integer retryCnt
) {

}
