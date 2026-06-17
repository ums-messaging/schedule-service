package com.ums.schedule.domain.schedule.status;

import com.ums.schedule.domain.schedule.code.ScheduleEventEnum;
import com.ums.schedule.domain.schedule.code.ScheduleStatusEnum;
import com.ums.schedule.domain.schedule.exception.InvalidScheduleStatusException;
import com.ums.schedule.common.converter.StatusState;
import com.ums.schedule.domain.schedule.state.ScheduleInActiveStatus;
import com.ums.schedule.domain.schedule.state.ScheduleStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScheduleInActiveStatusTest {
    @Test
    @DisplayName("InActive 상태에서 Active 상태로 변경 가능하다.")
    void shouldReturnInactiveToActive() {
        ScheduleStatus status = new ScheduleInActiveStatus();
        StatusState toStatus = status.onEvent(ScheduleEventEnum.TO_ACTIVE);
        assertThat(toStatus.getCurrentCode())
                .isEqualTo(ScheduleStatusEnum.ACTIVE);
    }

    @Test
    @DisplayName("InActive 상태에서 Running 상태로 변경 불가능하다.")
    void shouldRejectInActiveChangeToRunning() {
        ScheduleStatus status = new ScheduleInActiveStatus();
        assertThatThrownBy(() -> status.onEvent(ScheduleEventEnum.TO_RUNNING))
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("InActive 상태에서 InActive 상태로 변경 불가능하다.")
    void shouldRejectInActiveChangeToInActive() {
        ScheduleStatus status = new ScheduleInActiveStatus();
        assertThatThrownBy(() -> status.onEvent(ScheduleEventEnum.TO_INACTIVE))
                .isInstanceOf(InvalidScheduleStatusException.class);
    }
}