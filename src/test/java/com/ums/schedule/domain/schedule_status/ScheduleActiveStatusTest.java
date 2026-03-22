package com.ums.schedule.domain.schedule_status;

import com.ums.schedule.common.code.ScheduleStatusEnum;
import com.ums.schedule.domain.exception.InvalidScheduleStatusException;
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

    @Test
    @DisplayName("Active 상태에서 PAUSE 상태로 변경 불가능하다.")
    void shouldRejectActiveChangeToPause() {
        ScheduleStatus status = new ScheduleActiveStatus();
        assertThatThrownBy(()->status.toPause())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("Active 상태에서 STOP 상태로 변경 불가능하다.")
    void shouldRejectActiveChangeToStop() {
        ScheduleStatus status = new ScheduleActiveStatus();
        assertThatThrownBy(() -> status.toStop())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("Active 상태에서 COMPLETED 상태로 변경 불가능하다.")
    void shouldRejectActiveChangeToCompleted() {
        ScheduleStatus status = new ScheduleActiveStatus();
        assertThatThrownBy(() -> status.toCompleted())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

}