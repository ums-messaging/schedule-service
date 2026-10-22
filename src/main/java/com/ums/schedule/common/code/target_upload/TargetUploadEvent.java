package com.ums.schedule.common.code.target_upload;

import com.ums.schedule.common.converter.state.StatusStateEvent;
import com.ums.schedule.common.converter.state.StatusStateType;
import com.ums.schedule.orchestrator.common.code.SendRequestUploadStatus;

public enum TargetUploadEvent implements StatusStateEvent {
    TARGET_UPLOAD_CREATED(SendRequestUploadStatus.CREATED, "대상자 업로드 생성됨"),
    PROVIDED_URL(SendRequestUploadStatus.WAITING, "대상자 업로드 준비됨"),
    FILE_UPLOAD_REQUEST(SendRequestUploadStatus.REQUEST, "대상자 업로드 요청됨"),
    TARGET_UPLOAD_COMPLETED(SendRequestUploadStatus.COMPLETED, "대상자 업로드 완료됨"),
    ;

    SendRequestUploadStatus status;
    String description;

    TargetUploadEvent(SendRequestUploadStatus status, String description) {
        this.status = status;
        this.description = description;
    }

    @Override
    public String code() {
        return this.name();
    }

    @Override
    public String value() {
        return this.status.code();
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public StatusStateType stateType() {
        return this.status;
    }
}