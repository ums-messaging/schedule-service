package com.ums.schedule.fixture.target_upload;

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
    private TargetUploadType uploadType = TargetUploadType.FILE;
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

    public static TargetUploadReportEntityBuilder builder() {
        return new TargetUploadReportEntityBuilder();
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
                objectKey,
                createdAt,
                requestedAt,
                uploadedAt,
                sendRequest);
    }
}
