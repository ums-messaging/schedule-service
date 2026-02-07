package com.ums.schedule.domain.schedule_status;

import com.ums.schedule.common.code.ScheduleStatusEnum;
import com.ums.schedule.domain.exception.InvalidScheduleStatusException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ScheduleCompletedStatusTest {
    @Test
    @DisplayName("Completed 상태에서 Active 상태로 변경 불가능하다.")
    void shouldRejectChangeToActive() {
        ScheduleStatus status = new ScheduleCompletedStatus();
        assertThatThrownBy(() -> status.toActive())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("Completed 상태에서 Running 상태로 변경 가능하다.")
    void shouldReturnCompletedChangeToRunning() {
        ScheduleStatus status = new ScheduleCompletedStatus();
        ScheduleStatus toStatus = status.toRunning();
        assertThat(toStatus.currentScheduleStatus()).isEqualTo(ScheduleStatusEnum.RUNNING);
    }

    @Test
    @DisplayName("Completed 상태에서 Sending 상태로 변경 불가능하다.")
    void scheduleRejectChangeToSending() {
        ScheduleStatus status = new ScheduleCompletedStatus();
        assertThatThrownBy(() -> status.toSending())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("Completed 상태에서 PAUSE 상태로 변경 불가능하다.")
    void shouldRejectChangeToPause() {
        ScheduleStatus status = new ScheduleCompletedStatus();
        assertThatThrownBy(() -> status.toPause())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("Completed 상태에서 STOP 상태로 변경 불가능하다.")
    void shouldRejectChangeToStop() {
        ScheduleStatus status = new ScheduleCompletedStatus();
        assertThatThrownBy(() -> status.toStop())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("Completed 상태에서 Completed 상태로 변경 불가능하다.")
    void shouldRejectChangeToCompleted() {
        ScheduleStatus status = new ScheduleCompletedStatus();
        assertThatThrownBy(() -> status.toCompleted())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("Completed 상태에서 Inactive 상태로 변경 불가능하다.")
    void shouldRejectChangeToInactive() {
        ScheduleStatus status = new ScheduleCompletedStatus();
        assertThatThrownBy(() -> status.toInActive())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }
}