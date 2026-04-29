package com.ums.schedule.application.event.listener;

import com.ums.schedule.domain.target.event.TargetUploadCompletedEvent;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.target.upload.TargetUploadRepository;
import com.ums.schedule.domain.target.upload.TargetUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TargetUploadCompletedEventListener {
    private final TargetUploadRepository uploadRepository;

    @EventListener
    public void listen(TargetUploadCompletedEvent event) {
        TargetUpload targetUpload = uploadRepository.findById(event.uploadId()).orElseThrow();
        SendRequest request = targetUpload.getSendRequest();
        targetUpload.uploadComplete(event.uploadTotalSize());
    }
}
