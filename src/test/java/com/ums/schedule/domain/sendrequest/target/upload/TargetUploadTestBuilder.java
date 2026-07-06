package com.ums.schedule.domain.sendrequest.target.upload;

import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadFormatEnum;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import com.ums.schedule.domain.sendrequest.target.upload.state.TargetUploadCreateState;
import com.ums.schedule.domain.sendrequest.target.upload.state.TargetUploadState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TargetUploadTestBuilder {
    private Long id;
    private TargetUploadTypeEnum uploadType = TargetUploadTypeEnum.FILE;
    private TargetUploadState uploadStatus = new TargetUploadCreateState();
    private TargetUploadFormatEnum format = TargetUploadFormatEnum.CSV;
    private String resultMessage ;
    private Long totalCount;
    private Long fileSize = 100L;
    private String objectKey = UUID.randomUUID().toString();
    private LocalDateTime uploadedAt = LocalDateTime.now();
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime requestedAt = LocalDateTime.now();
    private SendRequest sendRequest;
    private List<SendTarget> targetList = new ArrayList<>();

    public static TargetUploadTestBuilder builder() {
        return new TargetUploadTestBuilder();
    }

    public TargetUploadTestBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public TargetUploadTestBuilder uploadType(TargetUploadTypeEnum uploadType) {
        this.uploadType = uploadType;
        return this;
    }

    public TargetUploadTestBuilder uploadFormat(TargetUploadFormatEnum uploadFormat) {
        this.format = uploadFormat;
        return this;
    }

    public TargetUploadTestBuilder uploadStatus(TargetUploadState state) {
        this.uploadStatus = state;
        return this;
    }

    public TargetUploadTestBuilder objectKey(String objectKey) {
        this.objectKey = objectKey;
        return this;
    }

    public TargetUploadTestBuilder totalCount(Long totalCount) {
        this.totalCount = totalCount;
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

    public TargetUploadReport build() {
        return new TargetUploadReport(
                id,
                uploadType,
                format,
                totalCount,
                0L,
                0L,
                null,
                uploadStatus,
                resultMessage,
                fileSize,
                objectKey,
                objectKey,
                createdAt,
                requestedAt,
                uploadedAt,
                sendRequest);
    }
}
