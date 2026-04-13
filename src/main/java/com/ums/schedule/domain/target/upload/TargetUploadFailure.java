package com.ums.schedule.domain.target.upload;

import com.ums.schedule.domain.target.upload.TargetUpload;

import java.time.LocalDateTime;

public class TargetUploadFailure {
    private String targetKey;
    private String contact;
    private String targetName;
    private String messageVariable;
    private String errorCode;
    private String errorMessage;
    private Long rowNo;
    private LocalDateTime createdAt;
    private TargetUpload targetUpload;
}
