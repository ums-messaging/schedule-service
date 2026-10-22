package com.ums.schedule.email.application.send.job.model;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.email.application.send.job.config.SendJobConfig;
import com.ums.schedule.email.domain.requests.EmailSendRequest;
import com.ums.schedule.email.domain.requests.sender.Sender;

import java.util.UUID;

public record SendJob (
    UUID jobId,
    Long requestId,
    Long sendRequestId,
    MessageSendData messageData,
    Sender sender,
    Integer batchSize,
    Integer partitionSize
) {
    public static SendJob of(EmailSendRequest request, MessageSendData messageData, SendJobConfig config) {
        return new SendJob(
                UuidCreator.getTimeOrdered(),
                request.getRequestId(),
                request.getId(),
                messageData,
                request.getSender(),
                config.batchSize(),
                config.partitionSize()
        );
    }
}
