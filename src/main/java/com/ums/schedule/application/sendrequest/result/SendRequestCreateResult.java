package com.ums.schedule.application.sendrequest.result;

import com.ums.schedule.application.sendrequest.target.result.TargetUploadResult;
import com.ums.schedule.domain.sendrequest.SendRequest;

import java.time.LocalDateTime;

public record SendRequestCreateResult(
        Long requestId,
        Long uploadId,
        String channelType,
        String uploadType,
        Long totalCount,
        Long successCount,
        Long failCount,
        String objectKey,
        String presignedUrl,
        LocalDateTime expiredAt
) {

    public static SendRequestCreateResult of(SendRequest sendRequest, TargetUploadResult result) {
        return new SendRequestCreateResult(
                sendRequest.getId(),
                sendRequest.getCurrentUploadId(),
                sendRequest.getChannelType().code(),
                sendRequest.getCurrentTargetUpload().getUploadType().code(),
                result.jsonTargetUploadResult().totalCount(),
                result.jsonTargetUploadResult().successCount(),
                result.jsonTargetUploadResult().failCount(),
                result.fileUploadResult().objectKey(),
                result.fileUploadResult().uploadUrl(),
                result.fileUploadResult().expiredAt()
        );
    }
}
