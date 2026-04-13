package com.ums.schedule.domain.target.upload.status;

import com.ums.schedule.TargetUploadStatusEnum;

public class TargetParsingStatus implements TargetUploadStatus {

    @Override
    public TargetUploadStatusEnum currentStatus() {
        return TargetUploadStatusEnum.PARSING;
    }
}
