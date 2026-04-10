package com.ums.schedule.send.domain.request.upload;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.send.application.model.dto.TargetUploadDto;
import com.ums.schedule.send.code.TargetUploadStatusEnum;
import com.ums.schedule.send.code.TargetUploadTypeEnum;
import com.ums.schedule.send.domain.reporing.SendReport;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.request.upload.status.*;
import com.ums.schedule.send.domain.target.EmailSendTarget;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ums.schedule.send.code.TargetUploadStatusEnum.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TargetUpload {
    private String uploadId;
    private TargetUploadTypeEnum uploadType;
    private SendReport report;
    private TargetUploadStatus uploadStatus;
    private TargetUploadStatusEnum status;
    private Long fileSize;
    private String objectKey;
    private LocalDateTime uploadedAt;
    private LocalDateTime createdAt;

    @ManyToOne
    private SendRequest sendRequest;

    @OneToMany
    private List<EmailSendTarget> targetList = new ArrayList<>();

    public static TargetUpload of(TargetUploadTypeEnum uploadType, SendRequest request) {
        TargetUpload targetUpload = new TargetUpload();
        targetUpload.changeStatus(new TargetCreatedStatus());
        targetUpload.resolveUploadType(uploadType);
        targetUpload.applySendRequest(request);
        return targetUpload;
    }

    private void resolveUploadType(TargetUploadTypeEnum uploadType) {
        this.uploadType = uploadType;
    }

    public TargetUploadStatus changeStatus(TargetUploadStatus uploadStatus) {
        this.uploadStatus = uploadStatus;
        this.status = uploadStatus.currentStatus();
        return uploadStatus;
    }

    public void applySendRequest(SendRequest sendRequest) {
        this.sendRequest = sendRequest;
        this.sendRequest.getTargetUploadList().add(this);
//        this.report = SendReport.of(sendRequest.getTargetList().size());
    }

    public boolean isProcess() {
        return status == CREATED || status == UPLOAD || status == PARSING;
    }

    public TargetUploadStatus upload() {
        changeStatus(new TargetUploadedStatus());
        return this.uploadStatus;
    }

    public TargetUploadStatus parsing() {
        changeStatus(new TargetParsingStatus());
        return this.uploadStatus;
    }

    public TargetUploadStatus completed() {
        changeStatus(new TargetCompletedStatus());
        return this.uploadStatus;
    }

    public void applyObjectKey(String objectKey) {
        this.objectKey = objectKey;
        this.createdAt = LocalDateTime.now();
    }
}
