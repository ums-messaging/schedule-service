package com.ums.schedule.common.code.target_upload;

import com.ums.schedule.common.converter.state.StatusState;
import com.ums.schedule.common.converter.state.StatusStateType;
import com.ums.schedule.domain.target.upload.state.*;

public enum TargetUploadStatus implements StatusStateType {
    CREATED("CREATED", "대상자 업로드 생성") {
        @Override
        public StatusState createStatus() {
            return new TargetUploadCreateState();
        }
    },
    WAITING("WAITING", "대상자 업로드 대기") {
        @Override
        public StatusState createStatus() {
            return new TargetUploadWaitingState();
        }
    },
    REQUEST("REQUEST", "대상자 업로드 요청") {
        @Override
        public StatusState createStatus() {
            return new TargetUploadRequestState();
        }
    },
    PARSING("PARSING", "대상자 처리 진행 중") {
        @Override
        public StatusState createStatus() {
            return new TargetUploadParsingState();
        }
    },
    COMPLETED("COMPLETE", "처리 완료") {
        @Override
        public StatusState createStatus() {
            return new TargetUploadCompleteState();
        }
    },
    FAIL("FAIL", "처리 실패") {
        @Override
        public StatusState createStatus() {
            return new TargetUploadFailState();
        }
    }
    ;

    String value;
    String description;

    TargetUploadStatus(String value, String description) {
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
}
