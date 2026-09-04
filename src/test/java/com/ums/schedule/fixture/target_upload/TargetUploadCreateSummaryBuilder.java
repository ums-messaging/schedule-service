package com.ums.schedule.fixture.target_upload;

import com.ums.schedule.application.ums.common.request.model.TargetUploadCreateSummary;
import com.ums.schedule.common.code.target_upload.TargetUploadStatus;
import com.ums.schedule.common.code.target_upload.TargetUploadType;

import java.time.Instant;

public class TargetUploadCreateSummaryBuilder {
    private String uploadId;
    private TargetUploadType uploadType;
    private TargetUploadStatus status;
    private String objectKey;
    private String presignedUrl;
    private Instant expiredAt;

    public static TargetUploadCreateSummaryBuilder builder() {
        return new TargetUploadCreateSummaryBuilder();
    }

    public TargetUploadCreateSummaryBuilder uploadId(String uploadId) {
        this.uploadId = uploadId;
        return this;
    }

    public TargetUploadCreateSummaryBuilder uploadType(TargetUploadType uploadType) {
        this.uploadType = uploadType;
        return this;
    }

    public TargetUploadCreateSummaryBuilder status(TargetUploadStatus status) {
        this.status = status;
        return this;
    }

    public TargetUploadCreateSummaryBuilder objectKey(String objectKey) {
        this.objectKey = objectKey;
        return this;
    }

    public TargetUploadCreateSummaryBuilder presignedUrl(String presignedUrl) {
        this.presignedUrl = presignedUrl;
        return this;
    }

    public TargetUploadCreateSummaryBuilder expiredAt(Instant expiredAt) {
        this.expiredAt = expiredAt;
        return this;
    }

    public TargetUploadCreateSummary build() {
        return new TargetUploadCreateSummary(
                this.uploadId,
                this.uploadType,
                this.status,
                this.objectKey,
                this.presignedUrl,
                this.expiredAt
        );
    }
}
