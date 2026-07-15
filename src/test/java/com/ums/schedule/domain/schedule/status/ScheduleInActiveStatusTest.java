package com.ums.schedule.domain.schedule.status;

import com.ums.schedule.common.code.schedule.ScheduleEvent;
import com.ums.schedule.common.code.schedule.ScheduleStatus;
import com.ums.schedule.domain.exception.schedule.InvalidScheduleStatusException;
import com.ums.schedule.common.converter.StatusState;
import com.ums.schedule.domain.schedule.state.ScheduleInActiveStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScheduleInActiveStatusTest {
    @Test
    @DisplayName("InActive 상태에서 Active 상태로 변경 가능하다.")
    void shouldReturnInactiveToActive() {
        com.ums.schedule.domain.schedule.state.ScheduleStatus status = new ScheduleInActiveStatus();
        StatusState toStatus = status.onEvent(ScheduleEvent.TO_ACTIVE);
        assertThat(toStatus.getCurrentCode())
                .isEqualTo(ScheduleStatus.ACTIVE);
    }

    @Test
    @DisplayName("InActive 상태에서 Running 상태로 변경 불가능하다.")
    void shouldRejectInActiveChangeToRunning() {
        com.ums.schedule.domain.schedule.state.ScheduleStatus status = new ScheduleInActiveStatus();
        assertThatThrownBy(() -> status.onEvent(ScheduleEvent.TO_RUNNING))
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("InActive 상태에서 InActive 상태로 변경 불가능하다.")
    void shouldRejectInActiveChangeToInActive() {
        com.ums.schedule.domain.schedule.state.ScheduleStatus status = new ScheduleInActiveStatus();
        assertThatThrownBy(() -> status.onEvent(ScheduleEvent.TO_INACTIVE))
                .isInstanceOf(InvalidScheduleStatusException.class);
    }
}