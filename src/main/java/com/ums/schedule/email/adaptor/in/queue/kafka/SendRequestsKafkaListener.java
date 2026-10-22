package com.ums.schedule.email.adaptor.in.queue.kafka;

import com.ums.schedule.email.adaptor.in.queue.kafka.payload.SendRequestPayload;
import com.ums.schedule.email.application.requests.EmailSendRequestSendService;
import com.ums.schedule.email.application.requests.result.SendRequestQueryResult;
import com.ums.schedule.email.application.send.job.SendJobManager;

import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.email.application.send.job.command.JobCommand;
import com.ums.schedule.email.domain.requests.EmailSendRequest;
import com.ums.schedule.email.domain.requests.result.SendRequestReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SendRequestsKafkaListener {
    private final EmailSendRequestSendService queryService;
    private final SendJobManager jobManager;

    @KafkaListener(
            topics = "email-send-requests",
            groupId = "email-target-upload-consumer",
            containerFactory = "emailSendListenerFactory"
    )
    public void listen(String json, Acknowledgment ack) {
        try {
            SendRequestPayload payload = JsonUtil.toObject(json, SendRequestPayload.class);
            SendRequestQueryResult result = queryService.findSendRequest(payload.requestId());
            EmailSendRequest sendRequest = result.sendRequest();
            JobCommand command = JobCommand.of(result, payload.requestId(), payload.downloadUrl());
            jobManager.send(command);
            ack.acknowledge();
        } catch (IllegalArgumentException e) {
            log.error("이미 처리됨 : {}", e);
            ack.acknowledge();
        } catch (Exception e) {
            // 오류로 업데이트 처리하기
            e.printStackTrace();
        }
    }
}
