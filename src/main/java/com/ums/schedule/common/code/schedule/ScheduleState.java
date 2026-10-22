package com.ums.schedule.common.code.schedule;

import com.ums.schedule.common.converter.state.StatusState;
import com.ums.schedule.common.converter.state.StatusStateType;
import com.ums.schedule.orchestrator.domain.schedule.state.ScheduleActiveStatus;
import com.ums.schedule.orchestrator.domain.schedule.state.ScheduleInActiveStatus;
import com.ums.schedule.orchestrator.domain.schedule.state.ScheduleRunningStatus;
import com.ums.schedule.orchestrator.domain.schedule.state.ScheduleStatus;

public enum ScheduleState implements StatusStateType {
    RUNNING("RUNNING","실행중") {
        @Override
        public ScheduleStatus createStatus() {
            return new ScheduleRunningStatus();
        }
    },
    ACTIVE("ACTIVE", "활성화") {
        @Override
        public ScheduleStatus createStatus() {
            return new ScheduleActiveStatus();
        }
    },
    INACTIVE("INACTIVE", "비활성화") {
        @Override
        public ScheduleStatus createStatus() {
            return new ScheduleInActiveStatus();
        }
    };

    String value;
    String description;

    ScheduleState(String value, String description) {
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


    @Override
    public ScheduleStatus createStatus() {
        return createStatus();
    }
}