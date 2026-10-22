package com.ums.schedule.orchestrator.domain.upload.event;

import com.ums.schedule.orchestrator.domain.upload.SendRequestUpload;

public interface SendRequestUploadEvent {
    void onEvent(SendRequestUpload upload);
}
