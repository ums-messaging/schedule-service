package com.ums.schedule.send.domain.request.upload.status;

import com.ums.schedule.send.code.TargetUploadStatusEnum;

public class TargetCompletedStatus implements TargetUploadStatus {

    @Override
    public TargetUploadStatusEnum currentStatus() {
        return TargetUploadStatusEnum.COMPLETED;
    }
}
