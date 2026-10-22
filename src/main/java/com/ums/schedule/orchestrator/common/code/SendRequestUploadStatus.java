package com.ums.schedule.orchestrator.common.code;

import com.ums.schedule.orchestrator.domain.upload.state.*;
import com.ums.schedule.common.converter.state.StatusStateType;

public enum SendRequestUploadStatus implements StatusStateType {
    CREATED("CREATED", "대상자 업로드 생성") {
        @Override
        public SendRequestUploadState createStatus() {
            return new SendRequestUploadCreateState();
        }
    },
    WAITING("WAITING", "대상자 업로드 대기") {
        @Override
        public SendRequestUploadState createStatus() {
            return new SendRequestUploadWaitingState();
        }
    },
    REQUEST("REQUEST", "대상자 업로드 요청") {
        @Override
        public SendRequestUploadState createStatus() {
            return new SendRequestUploadRequestState();
        }
    },
    COMPLETED("COMPLETE", "처리 완료") {
        @Override
        public SendRequestUploadState createStatus() {
            return new SendRequestUploadCompleteState();
        }
    }
    ;

    String value;
    String description;

    SendRequestUploadStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }


    @Override
    public String code() {
        return this.name();
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public String description() {
        return this.description;
    }

    public SendRequestUploadState createStatus() {
        return this.createStatus();
    }
}
