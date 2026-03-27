package com.ums.schedule.send.domain.target.upload.status;

import com.ums.schedule.send.code.TargetUploadStatusEnum;

public class TargetCreatedStatus implements TargetUploadStatus {

    @Override
    public TargetUploadStatusEnum currentStatus() {
        return TargetUploadStatusEnum.CREATED;
    }
}
