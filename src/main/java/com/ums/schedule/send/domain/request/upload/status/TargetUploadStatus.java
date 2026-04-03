package com.ums.schedule.send.domain.request.upload.status;

import com.ums.schedule.send.code.TargetUploadStatusEnum;

// CREATE->UPLOADED(UPLOAD_FAIL)-> PARSING(OR FAIL)->COMPLETED
public interface TargetUploadStatus {
    TargetUploadStatusEnum currentStatus();
}
