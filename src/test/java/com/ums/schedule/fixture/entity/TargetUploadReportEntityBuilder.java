package com.ums.schedule.fixture.entity;

import com.ums.schedule.common.code.target_upload.TargetUploadType;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.common.code.target_upload.TargetUploadFormatEnum;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.upload.state.TargetUploadCreateState;
import com.ums.schedule.domain.target.upload.state.TargetUploadState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TargetUploadReportEntityBuilder {
    private UUID id;
    private TargetUploadType uploadType;
    private TargetUploadState uploadStatus;
    private TargetUploadFormatEnum format;
    private String resultMessage ;
    private Long totalCount;
    private Long fileSize;
    private String objectKey = UUID.randomUUID().toString();
    private String downloadKey;
    private LocalDateTime uploadedAt ;
    private LocalDateTime createdAt;
    private LocalDateTime requestedAt;
    private SendRequest sendRequest;

    public static TargetUploadReportEntityBuilder builder() {
        return new TargetUploadReportEntityBuilder();
    }

    private TargetUploadReportEntityBuilder() {
        this.id = UUID.randomUUID();
        uploadType = TargetUploadType.JSON;
        downloadKey = "/target/upload/download/target.xlsx";
        uploadStatus = new TargetUploadCreateState();
        createdAt = LocalDateTime.now();
    }

    public TargetUploadReportEntityBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    public TargetUploadReportEntityBuilder uploadType(TargetUploadType uploadType) {
        this.uploadType = uploadType;
        return this;
    }

    public TargetUploadReportEntityBuilder uploadFormat(TargetUploadFormatEnum uploadFormat) {
        this.format = uploadFormat;
        return this;
    }

    public TargetUploadReportEntityBuilder downloadKey(String downloadKey) {
        this.downloadKey = downloadKey;
        return this;
    }
    public TargetUploadReportEntityBuilder uploadStatus(TargetUploadState state) {
        this.uploadStatus = state;
        return this;
    }

    public TargetUploadReportEntityBuilder objectKey(String objectKey) {
        this.objectKey = objectKey;
        return this;
    }

    public TargetUploadReportEntityBuilder totalCount(Long totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public TargetUploadReportEntityBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public TargetUploadReportEntityBuilder sendRequest(SendRequest request) {
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
                downloadKey,
                createdAt,
                requestedAt,
                uploadedAt,
                sendRequest, true);
    }
}