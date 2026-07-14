package com.ums.schedule.common.code.schedule;

import com.ums.schedule.common.converter.StatusState;
import com.ums.schedule.common.converter.StatusStateFactory;
import com.ums.schedule.domain.schedule.state.ScheduleActiveStatus;
import com.ums.schedule.domain.schedule.state.ScheduleInActiveStatus;
import com.ums.schedule.domain.schedule.state.ScheduleRunningStatus;

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
