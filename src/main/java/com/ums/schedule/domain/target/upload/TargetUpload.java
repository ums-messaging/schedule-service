package com.ums.schedule.domain.target.upload;

import com.ums.schedule.application.target.dto.SendTargetDto;
import com.ums.schedule.code.send.TargetUploadStatusEnum;
import com.ums.schedule.code.send.TargetUploadTypeEnum;
import com.ums.schedule.domain.channel.ChannelTemplate;
import com.ums.schedule.domain.request.SendRequest;

import com.ums.schedule.domain.request.SendRequestEvent;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.event.*;
import com.ums.schedule.domain.target.exeption.TargetMessageCreatedEventException;
import com.ums.schedule.domain.target.exeption.upload.TargetUploadException;
import com.ums.schedule.domain.target.exeption.upload.TargetUploadObjectKeyRequiredException;
import com.ums.schedule.domain.target.exeption.upload.TargetUploadCompleteStateException;
import com.ums.schedule.domain.target.state.upload.TargetUploadCreateState;
import com.ums.schedule.domain.target.state.upload.TargetUploadState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TargetUpload {
    @Id
    private Long uploadId;

    @Enumerated(EnumType.STRING)
    private TargetUploadTypeEnum uploadType;

    private Integer totalCount;

    @Transient
    private TargetUploadState uploadStatus;

    @Enumerated(EnumType.STRING)
    private TargetUploadStatusEnum status;
    private String resultMessage;

    private Long fileSize;
    private String objectKey;

    private LocalDateTime uploadedAt;
    private LocalDateTime createdAt;

    @ManyToOne
    private SendRequest sendRequest;

    @OneToMany
    private List<SendTarget> targetList = new ArrayList<>();

    public static TargetUpload of(TargetUploadTypeEnum uploadType, SendRequest sendRequest) {
        TargetUpload targetUpload = new TargetUpload();
        targetUpload.changeStatus(new TargetUploadCreateState());
        targetUpload.resolveUploadType(uploadType);
        targetUpload.applySendRequest(sendRequest);
        return targetUpload;
    }

    private void resolveUploadType(TargetUploadTypeEnum uploadType) {
        this.uploadType = uploadType;
    }

    private TargetUploadEvent onEvent(TargetUploadEvent event) {
        TargetUploadState toStatus = this.uploadStatus.onEvent(event);
        changeStatus(toStatus);
        return event;
    }

    private TargetUploadState changeStatus(TargetUploadState uploadStatus) {
        this.uploadStatus = uploadStatus;
        this.status = uploadStatus.currentStatus();
        return uploadStatus;
    }

    private void applySendRequest(SendRequest sendRequest) {
        this.sendRequest = sendRequest;
        this.sendRequest.addTargetUploadList(this);
    }

    public TargetUploadEvent createTargetUploadUrlEvent(String objectKey) {
        if(uploadType == TargetUploadTypeEnum.FILE && !StringUtils.hasText(objectKey)) {
            throw TargetUploadObjectKeyRequiredException.of();
        }
        this.objectKey = objectKey;
        this.uploadedAt = LocalDateTime.now();
        return onEvent(TargetUploadUrlCreatedEvent.of(this));
    }

    public SendRequestEvent requestTargetUpload() {
        TargetUploadRequestedEvent event = TargetUploadRequestedEvent.of(this);
        onEvent(event);
        return SendRequestEvent.of(sendRequest, event);
    }

    public TargetUploadEvent parseMessage(ChannelTemplate template, List<SendTargetDto> targetDtos) {
        if(template == null) {
            throw TargetMessageCreatedEventException.ofTemplate();
        }
        return onEvent(TargetMessageCreatedEvent.of(this, template, targetDtos));
    }

    public TargetUploadEvent addTargetList(SendTarget target) {
        this.targetList.add(target);
        return onEvent(TargetUploadUploadedEvent.of(this));
    }

    public SendRequestEvent uploadComplete(int totalSize) {
        if(this.targetList.size() == totalSize) {
            TargetUploadCompletedEvent event = TargetUploadCompletedEvent.of(this);
            this.totalCount = this.targetList.size();
            onEvent(event);
            return SendRequestEvent.of(sendRequest, event);
        }
        throw TargetUploadCompleteStateException.ofDifferentTargetSize(totalSize, this.targetList.size());
    }

    public TargetUploadEvent onError(TargetUploadException ex) {
        TargetUploadFailedEvent event = TargetUploadFailedEvent.of(this, ex.getMessage());
        TargetUploadState state = this.uploadStatus.onFail();
        changeStatus(state);
        applyResultMessage(ex.getMessage());
        return event;
    }

    private void applyResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public SendRequestEvent assignTargetUpload() {
        if(this.status != TargetUploadStatusEnum.COMPLETED) {
            throw TargetUploadCompleteStateException.targetUploadUnComplete();
        }
        TargetUploadRequestedEvent event = TargetUploadRequestedEvent.of(this);
        this.sendRequest.assignToTargetUpload(this);
        return SendRequestEvent.of(this.sendRequest, event);
    }
}