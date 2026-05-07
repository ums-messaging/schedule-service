package com.ums.schedule.domain.target.upload;

import com.ums.schedule.code.send.TargetUploadStatusEnum;
import com.ums.schedule.code.send.TargetUploadTypeEnum;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.state.upload.TargetUploadCreateState;
import com.ums.schedule.domain.target.state.upload.TargetUploadState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TargetUploadTestBuilder {
    private TargetUploadTypeEnum uploadType = TargetUploadTypeEnum.FILE;
    private TargetUploadState uploadStatus = new TargetUploadCreateState();
    private TargetUploadStatusEnum status = TargetUploadStatusEnum.CREATED;
    private String resultMessage ;
    private Long fileSize = 100L;
    private String objectKey = UUID.randomUUID().toString();
    private LocalDateTime uploadedAt = LocalDateTime.now();
    private LocalDateTime createdAt = LocalDateTime.now();
    private SendRequest sendRequest;
    private List<SendTarget> targetList = new ArrayList<>();

    public static TargetUploadTestBuilder builder() {
        return new TargetUploadTestBuilder();
    }

    public TargetUploadTestBuilder uploadType(TargetUploadTypeEnum uploadType) {
        this.uploadType = uploadType;
        return this;
    }

    public TargetUploadTestBuilder uploadStatus(TargetUploadState state) {
        this.status = (state == null) ? null : state.currentStatus();
        return this;
    }

    public TargetUploadTestBuilder objectKey(String objectKey) {
        this.objectKey = objectKey;
        return this;
    }

    public TargetUploadTestBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public TargetUploadTestBuilder sendRequest(SendRequest request) {
        this.sendRequest = request;
        return this;
    }

    public TargetUpload build() {
        return new TargetUpload(
                null,
                uploadType,
                uploadStatus,
                status,
                resultMessage,
                fileSize,
                objectKey,
                uploadedAt,
                createdAt,
                sendRequest,
                targetList);
    }



}
