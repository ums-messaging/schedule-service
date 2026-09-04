package com.ums.schedule.common.code.request;

import com.ums.schedule.domain.request.state.*;
import com.ums.schedule.common.converter.state.StatusState;
import com.ums.schedule.common.converter.state.StatusStateType;

public enum SendRequestStatus implements StatusStateType {
    CREATE("C", "발송 요청 등록") {
        @Override
        public StatusState createStatus() {
            return new SendRequestCreateState();
        }
    },
    HOLDING("H", "업로드 대기") {
        @Override
        public StatusState createStatus() {
            return new SendRequestHoldingState();
        }
    },
    REQUEST("Q", "발송요청") {
        @Override
        public StatusState createStatus() {
            return new SendRequestRequestState();
        }
    },
    READY("R", "발송준비") {
        @Override
        public StatusState createStatus() {
            return new SendRequestReadyState();
        }
    },
    CANCEL("S", "발송 요청 취소") {
        @Override
        public StatusState createStatus() {
            return new SendRequestCancelState();
        }
    },
    SENDING("I", "발송중") {
        @Override
        public StatusState createStatus() {
            return new SendRequestSendingState();
        }
    },
    COMPLETED("E", "발송완료") {
        @Override
        public StatusState createStatus() {
            return new SendRequestCompleteState();
        }
    },
    PAUSE("P", "일시중지") {
        @Override
        public StatusState createStatus() {
            return new SendRequestPauseState();
        }
    },
    ERROR("O", "발송에러") {
        @Override
        public StatusState createStatus() {
            return new SendRequestFailState();
        }
    };

    String value;
    String description;

    SendRequestStatus(String value, String description) {
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