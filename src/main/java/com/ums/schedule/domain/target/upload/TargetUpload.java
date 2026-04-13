package com.ums.schedule.domain.target.upload;

import com.ums.schedule.TargetUploadStatusEnum;
import com.ums.schedule.TargetUploadTypeEnum;
import com.ums.schedule.domain.target.SendReport;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.upload.status.*;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TargetUpload {
    private String uploadId;
    private TargetUploadTypeEnum uploadType;
    @ManyToOne
    private SendReport report;
    @ManyToOne
    private TargetUploadStatus uploadStatus;
    private TargetUploadStatusEnum status;
    private Long fileSize;
    private String objectKey;
    private LocalDateTime uploadedAt;
    private LocalDateTime createdAt;

    @ManyToOne
    private SendRequest sendRequest;

    @OneToMany
    private List<SendTarget> targetList = new ArrayList<>();

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
        return status == TargetUploadStatusEnum.CREATED || status == TargetUploadStatusEnum.UPLOAD || status == TargetUploadStatusEnum.PARSING;
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
