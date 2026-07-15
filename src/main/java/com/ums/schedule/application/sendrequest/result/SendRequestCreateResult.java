package com.ums.schedule.application.sendrequest.result;

import com.ums.schedule.application.sendrequest.target.result.FileTargetUploadResult;
import com.ums.schedule.application.sendrequest.target.result.TargetUploadResult;
import com.ums.schedule.domain.request.SendRequest;

import java.time.LocalDateTime;
import java.util.Optional;

public record SendRequestCreateResult(
        Long requestId,
        String uploadId,
        String channelType,
        Integer retryCount,
        String templateKey,
        String senderKey,
        String uploadType,
        String objectKey,
        String presignedUrl,
        LocalDateTime expiredAt
) {

    public static SendRequestCreateResult of(SendRequest sendRequest, TargetUploadResult result) {
        return Optional.ofNullable(result.fileUploadResult())
                .map(fileResult -> SendRequestCreateResult.of(sendRequest, result, fileResult))
                .orElseGet(() -> new SendRequestCreateResult(
                        sendRequest.getId(),
                        result.reportId(),
                        sendRequest.getChannelType().code(),
                        sendRequest.getRetryCnt(),
                        sendRequest.getTemplateKey(),
                        sendRequest.getSenderKey(),
                        result.uploadType(),
                        null,
                        null,
                        null
                ));
    }

    private static SendRequestCreateResult of(SendRequest sendRequest, TargetUploadResult uploadResult, FileTargetUploadResult fileResult)  {
        return new SendRequestCreateResult(
                sendRequest.getId(),
                uploadResult.reportId(),
                sendRequest.getChannelType().code(),
                sendRequest.getRetryCnt(),
                sendRequest.getTemplateKey(),
                sendRequest.getSenderKey(),
                uploadResult.uploadType(),
                fileResult.objectKey(),
                fileResult.uploadUrl(),
                fileResult.expiredAt()
        );
    }
}
