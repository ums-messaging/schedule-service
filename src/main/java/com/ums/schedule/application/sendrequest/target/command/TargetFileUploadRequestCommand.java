package com.ums.schedule.application.sendrequest.target.command;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;

import java.util.List;

public record TargetFileUploadRequestCommand(
        String templateId,
        String objectKey,
        String fileName,
        Long maxFileSize,
        Long fileSize,
        boolean isExistFile,
        List<TargetMessageData> targetList
) {
}
