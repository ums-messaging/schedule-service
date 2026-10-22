package com.ums.schedule.email.adaptor.in.queue.kafka.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SendRequestPayload (
        Long requestId,
        String downloadUrl
) {
}
