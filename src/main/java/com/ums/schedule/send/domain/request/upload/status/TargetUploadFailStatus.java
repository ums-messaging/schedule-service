package com.ums.schedule.send.domain.request.upload.status;

import com.ums.schedule.send.code.TargetUploadStatusEnum;

public class TargetUploadFailStatus implements TargetUploadStatus {

    @Override
    public TargetUploadStatusEnum currentStatus() {
        return TargetUploadStatusEnum.FAIL;
    }
}
