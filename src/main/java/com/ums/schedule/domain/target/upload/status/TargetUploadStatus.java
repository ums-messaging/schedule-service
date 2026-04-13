package com.ums.schedule.domain.target.upload.status;

import com.ums.schedule.TargetUploadStatusEnum;

// CREATE->UPLOADED(UPLOAD_FAIL)-> PARSING(OR FAIL)->COMPLETED
public interface TargetUploadStatus {
    TargetUploadStatusEnum currentStatus();
}
