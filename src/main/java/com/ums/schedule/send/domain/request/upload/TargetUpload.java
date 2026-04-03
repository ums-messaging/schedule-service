package com.ums.schedule.send.domain.request.upload;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.send.application.model.response.PresigedUrlResponse;
import com.ums.schedule.send.code.TargetUploadStatusEnum;
import com.ums.schedule.send.code.TargetUploadTypeEnum;
import com.ums.schedule.send.domain.reporing.SendReport;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.request.upload.status.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ums.schedule.send.code.TargetUploadStatusEnum.*;

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
    private LocalDateTime uploadAt;
    private SendRequest sendRequest;

    public static TargetUpload of(EnumMapperValue uploadType) {
        TargetUpload targetUpload = new TargetUpload();
        targetUpload.changeStatus(new TargetCreatedStatus());
        targetUpload.resolveUploadType(uploadType);
        return targetUpload;
    }

    private void resolveUploadType(EnumMapperValue uploadType) {
        this.uploadType = TargetUploadTypeEnum.valueOf(uploadType.code());
    }

    public TargetUploadStatus changeStatus(TargetUploadStatus uploadStatus) {
        this.uploadStatus = uploadStatus;
        this.status = uploadStatus.currentStatus();
        return uploadStatus;
    }

    public void applySendRequest(SendRequest sendRequest) {
        this.sendRequest = sendRequest;
        this.sendRequest.getTargetUploadList().add(this);
        this.report = SendReport.of(sendRequest.getTargetList().size());
    }

    public boolean isProcess() {
        return status == CREATED || status == UPLOAD || status == PARSING;
    }

    public void upload(PresigedUrlResponse response) {
        this.uploadAt = LocalDateTime.now();
        fromResponse(response);
        changeStatus(new TargetUploadedStatus());
    }

    public TargetUpload upload() {
        changeStatus(new TargetUploadedStatus());
        return this;
    }

    public TargetUpload parsing() {
        changeStatus(new TargetParsingStatus());
        return this;
    }

    public TargetUpload completed() {
        changeStatus(new TargetCompletedStatus());
        return this;
    }

    private void fromResponse(PresigedUrlResponse response) {
        this.fileSize = response.fileSize();
        this.objectKey = response.objectKey();
    }
}
