package com.ums.schedule.common.code.target;

import com.ums.schedule.common.converter.state.StatusState;
import com.ums.schedule.common.converter.state.StatusStateType;
import com.ums.schedule.domain.target.state.*;

public enum SendTargetStatus implements StatusStateType {
    CREATE("C", "생성") {
        @Override
        public StatusState createStatus() {
            return new SendTargetCreateState();
        }
    },
    READY("R", "준비") {
        @Override
        public StatusState createStatus() {
            return new SendTargetReadyState();
        }
    },
    RETRYING("T", "재시도") {
        @Override
        public StatusState createStatus() {
            return new SendTargetRetryState();
        }
    },
    FAIL("O", "실패") {
        @Override
        public StatusState createStatus() {
            return new SendTargetFailState();
        }
    },
    SENDING("S", "발송중") {
        @Override
        public StatusState createStatus() {
            return new SendTargetSendingState();
        }
    },
    COMPLETED("E", "완료") {
        @Override
        public StatusState createStatus() {
            return new SendTargetCompleteState();
        }
    };

    String value;
    String description;

    SendTargetStatus(String value, String description) {
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
