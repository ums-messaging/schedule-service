package com.ums.schedule.application.target.reader.model;

import com.ums.schedule.application.target.report.model.TargetUploadRequestResult;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.domain.target.upload.TargetUploadReport;

import java.util.UUID;

public record FileTargetUploadRequestedEvent(
        ChannelType channelType,
        Long requestId,
        UUID messageId,
        UUID uploadId,
        String uploadKey,
        Integer batchSize
) {

    public static FileTargetUploadRequestedEvent of(TargetUploadReport report, TargetUploadRequestResult result, Integer batchSize) {
        return new FileTargetUploadRequestedEvent(
                result.channelType(),
                result.requestId(),
                result.messageId(),
                report.getId(),
                report.getUploadKey(),
                batchSize
        );
    }
}
