package com.ums.schedule.application.sendrequest.target.event.listener;

import com.ums.schedule.adapter.api.target.context.TargetUploadContext;
import com.ums.schedule.application.sendrequest.data.SendRequestKeyData;
import com.ums.schedule.application.sendrequest.target.SendTargetUploadService;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.message.SendMessageJpaRepository;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.target.upload.TargetUploadReportJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendTargetUploadListener {
    private final SendMessageJpaRepository messageRepository;
    private final TargetUploadReportJpaRepository repository;
    private final SendTargetUploadService uploadService;
    @Async
    @EventListener
    public void listen(TargetUploadContext event) {
        TargetUploadReport findReport = repository.findById(event.targetUpload().getId()).orElseThrow();
        SendRequest sendRequest = findReport.getSendRequest();
        SendMessage sendMessage = messageRepository.findBySendRequest(sendRequest).orElseThrow();
        SendRequestKeyData keyData = SendRequestKeyData.of(sendMessage.getId().toString(), findReport);

        uploadService.upload(findReport, keyData, event.targetDataList(), event.targetDataList().size());
    }
}
