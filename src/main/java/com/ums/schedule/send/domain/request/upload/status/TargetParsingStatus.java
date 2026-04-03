package com.ums.schedule.send.domain.request.upload.status;

import com.ums.schedule.send.code.TargetUploadStatusEnum;

public class TargetParsingStatus implements TargetUploadStatus {

    @Override
    public TargetUploadStatusEnum currentStatus() {
        return TargetUploadStatusEnum.PARSING;
    }
}
