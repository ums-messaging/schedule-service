package com.ums.schedule.common.code.target_upload;

import com.ums.schedule.common.converter.state.StatusStateEvent;
import com.ums.schedule.common.converter.state.StatusStateType;

public enum TargetUploadEvent implements StatusStateEvent {
    TARGET_UPLOAD_CREATED(TargetUploadStatus.CREATED, "대상자 업로드 생성됨"),
    TARGET_UPLOAD_READY(TargetUploadStatus.WAITING, "대상자 업로드 준비됨"),
    TARGET_UPLOAD_REQUESTED(TargetUploadStatus.REQUEST, "대상자 업로드 요청됨"),
    TARGET_UPLOAD_STARTED(TargetUploadStatus.PARSING, "대상자 업로드 시작됨"),
    TARGET_UPLOAD_COMPLETED(TargetUploadStatus.COMPLETED, "대상자 업로드 완료됨"),
    TARGET_UPLOAD_FAIL(TargetUploadStatus.FAIL, "대상자 업로드 실패됨")
    ;

    TargetUploadStatus status;
    String description;

    TargetUploadEvent(TargetUploadStatus status, String description) {
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