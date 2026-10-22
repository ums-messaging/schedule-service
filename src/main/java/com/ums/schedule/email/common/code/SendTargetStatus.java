package com.ums.schedule.email.common.code;

import com.ums.schedule.common.converter.state.StatusState;
import com.ums.schedule.common.converter.state.StatusStateType;
import com.ums.schedule.email.domain.target.state.SendTargetCreateState;
import com.ums.schedule.email.domain.target.state.SendTargetErrorState;


public enum SendTargetStatus implements StatusStateType {
    CREATE("C", "생성") {
        @Override
        public StatusState createStatus() {
            return new SendTargetCreateState();
        }
    },
    ERROR("O", "실패") {
        @Override
        public StatusState createStatus() {
            return new SendTargetErrorState();
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
