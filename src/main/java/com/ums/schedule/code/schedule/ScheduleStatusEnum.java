package com.ums.schedule.code.schedule;

import com.ums.schedule.domain.state.StatusState;
import com.ums.schedule.domain.state.StatusStateFactory;
import com.ums.schedule.domain.state.schedule.ScheduleActiveStatus;
import com.ums.schedule.domain.state.schedule.ScheduleInActiveStatus;
import com.ums.schedule.domain.state.schedule.ScheduleRunningStatus;

public enum ScheduleStatusEnum implements StatusStateFactory {
    RUNNING("RUNNING","실행중") {
        @Override
        public StatusState createStatus() {
            return new ScheduleRunningStatus();
        }
    },
    ACTIVE("ACTIVE", "활성화") {
        @Override
        public StatusState createStatus() {
            return new ScheduleActiveStatus();
        }
    },
    INACTIVE("INACTIVE", "비활성화") {
        @Override
        public StatusState createStatus() {
            return new ScheduleInActiveStatus();
        }
    };

    String value;
    String description;

    ScheduleStatusEnum(String value, String description) {
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
