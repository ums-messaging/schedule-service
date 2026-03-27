package com.ums.schedule.send.domain.request;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.domain.SendMessage;
import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.domain.request.status.RequestCreateStatus;
import com.ums.schedule.send.domain.request.status.SendRequestStatus;
import com.ums.schedule.send.domain.target.upload.TargetUpload;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
public class SendRequestEvent {
    private SendRequestStatusEnum status;
    private SendRequestStatus sendRequestStatus;

    //    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;
    private LocalDateTime createdAt;
    private LocalDateTime scheduledAt;
    private LocalDateTime sendStartedAt;
    private LocalDateTime sendEndedAt;

    private String errorMessage;

    public SendRequestEvent() {
        changeSendRequestStatus(new RequestCreateStatus());
        this.createdAt = LocalDateTime.now();
    }

    private SendRequestStatus changeSendRequestStatus(SendRequestStatus sendRequestStatus) {
        this.sendRequestStatus = sendRequestStatus;
        this.status = sendRequestStatus.currentSendRequestStatus();
        return sendRequestStatus;
    }

    public void markScheduled() {
        changeSendRequestStatus(this.sendRequestStatus.toScheduled());
        this.scheduledAt = LocalDateTime.now();
    }

    private void markReady() {
        changeSendRequestStatus(this.sendRequestStatus.toReady());
    }

    public void markMessageCreated(SendRequest sendRequest) {
        TargetUpload targetUpload = getUploadingTarget(sendRequest.getTargetUploadList());
        if(targetUpload == null) {
            markReady();
        }
    }

    private TargetUpload getUploadingTarget(List<TargetUpload> targetUploadList) {
        return targetUploadList.stream()
                .filter(upload -> upload.isProcess())
                .findAny()
                .orElse(null);
    }

    public void markTargetUpload(SendRequest sendRequest) {
        Optional.ofNullable(sendRequest.getSendMessage())
                .filter(message -> message.getStatus().equals("ACTIVE"))
                .map(message -> {
                    markReady();
                    return sendRequest;
                });
    }

    public void markRequest() {
        changeSendRequestStatus(this.sendRequestStatus.toRequest());
        this.requestedAt = LocalDateTime.now();
    }

    public void markSend() {
        changeSendRequestStatus(this.sendRequestStatus.toSend());
        this.sendStartedAt = LocalDateTime.now();
    }

    public void markCompleted() {
        changeSendRequestStatus(this.sendRequestStatus.toComplete());
        this.sendEndedAt = LocalDateTime.now();
    }

    public void toError(EnumMapperValue errorCode) {
        changeSendRequestStatus(this.sendRequestStatus.toError());
        toError(errorCode.description());
    }
    public void toError(String message) {
        this.errorMessage = message;
    }
}
