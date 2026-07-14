package com.ums.schedule.domain.schedule.status;

import com.ums.schedule.common.code.schedule.ScheduleEventEnum;
import com.ums.schedule.common.code.schedule.ScheduleStatusEnum;
import com.ums.schedule.domain.exception.schedule.InvalidScheduleStatusException;
import com.ums.schedule.common.converter.StatusState;
import com.ums.schedule.domain.schedule.state.ScheduleActiveStatus;
import com.ums.schedule.domain.schedule.state.ScheduleStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScheduleActiveStatusTest {
    @Test
    @DisplayName("Active 상태에서 Active 상태로 변경 불가능하다.")
    void shouldRejectActiveChangeToActive() {
        ScheduleStatus status = new ScheduleActiveStatus();

        assertThatThrownBy(()-> status.onEvent(ScheduleEventEnum.TO_ACTIVE))
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("Active 상태에서 Running 상태로 변경한다.")
    void shouldReturnRunningChangeActive() {
        ScheduleStatus status = new ScheduleActiveStatus();
        StatusState toStatus = status.onEvent(ScheduleEventEnum.TO_RUNNING);
        assertThat(toStatus.getCurrentCode()).isEqualTo(ScheduleStatusEnum.RUNNING);
    }

    @Test
    @DisplayName("Active 상태에서 INACTIVE 상태로 변경한다.")
    void shouldReturnInActiveChangeActive() {
        ScheduleStatus status = new ScheduleActiveStatus();
        StatusState toStatus = status.onEvent(ScheduleEventEnum.TO_INACTIVE);
        assertThat(toStatus.getCurrentCode()).isEqualTo(ScheduleStatusEnum.INACTIVE);
    }
}