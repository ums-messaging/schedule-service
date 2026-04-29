package com.ums.schedule.intergration.schedule_status;

import com.ums.schedule.code.schedule.ScheduleStatusEnum;
import com.ums.schedule.domain.schedule.exception.InvalidScheduleStatusException;
import com.ums.schedule.domain.schedule.status.ScheduleActiveStatus;
import com.ums.schedule.domain.schedule.status.ScheduleStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScheduleActiveStatusTest {
    @Test
    @DisplayName("Active 상태에서 Active 상태로 변경 불가능하다.")
    void shouldRejectActiveChangeToActive() {
        ScheduleStatus status = new ScheduleActiveStatus();

        assertThatThrownBy(()->status.toActive())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("Active 상태에서 Running 상태로 변경한다.")
    void shouldReturnRunningChangeActive() {
        ScheduleStatus status = new ScheduleActiveStatus();
        ScheduleStatus toStatus = status.toRunning();
        assertThat(toStatus.currentScheduleStatus()).isEqualTo(ScheduleStatusEnum.RUNNING);
    }

    @Test
    @DisplayName("Active 상태에서 INACTIVE 상태로 변경한다.")
    void shouldReturnInActiveChangeActive() {
        ScheduleStatus status = new ScheduleActiveStatus();
        ScheduleStatus toStatus = status.toInActive();
        assertThat(toStatus.currentScheduleStatus()).isEqualTo(ScheduleStatusEnum.INACTIVE);
    }
}