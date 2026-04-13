package com.ums.schedule.domain.target.upload.status;

import com.ums.schedule.TargetUploadStatusEnum;

public class TargetCompletedStatus implements TargetUploadStatus {

    @Override
    public TargetUploadStatusEnum currentStatus() {
        return TargetUploadStatusEnum.COMPLETED;
    }
}
