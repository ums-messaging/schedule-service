package com.ums.schedule.email.application.send;

import com.ums.schedule.common.code.email.EmailResultCode;
import com.ums.schedule.common.code.request.SendGroupEventType;
import com.ums.schedule.email.adaptor.out.queue.kafka.SendGroupEventKafkaDispatcher;
import com.ums.schedule.email.application.event.SendGroupEventDispatcherFactory;
import com.ums.schedule.email.application.send.job.model.SendJob;
import com.ums.schedule.email.domain.common.SendResult;
import com.ums.schedule.email.domain.event.SendGroupEventResult;
import com.ums.schedule.email.domain.event.SendTargetResultList;
import com.ums.schedule.email.domain.requests.EmailSendRequest;
import com.ums.schedule.email.domain.requests.EmailSendRequestRepository;
import com.ums.schedule.email.domain.requests.result.SendResultReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendRequestUploadManager {
    private final EmailSendRequestRepository repository;
    private final SendGroupEventKafkaDispatcher dispatcher;

    @Transactional
    public void upload(SendJob job, SendResult sendResult,  SendTargetResultList resultList) {
        EmailSendRequest sendRequest = repository.findById(job.sendRequestId()).orElseThrow();
        SendResultReport report = SendResultReport.of(resultList);
        sendRequest.endSend(sendResult, report);
        EmailSendRequest saved = repository.save(sendRequest);
        log.info("saved = {}", saved.toString());
        SendGroupEventResult result = SendGroupEventResult.of(EmailResultCode.SUCCESS, null);
        dispatcher.publish(SendGroupEventType.SEND_RESULT_UPLOAD, result, resultList);
    }
}
