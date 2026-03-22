package com.ums.schedule.domain.schedule_status;

import com.ums.schedule.common.code.ScheduleStatusEnum;
import com.ums.schedule.domain.exception.InvalidScheduleStatusException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ScheduleSendingScheduleTest {
    @Test
    @DisplayName("Sending 상태에서 Sending 상태로 변경 불가능하다.")
    void shouldRejectSendingChangeToSending() {
        ScheduleStatus status = new ScheduleActiveStatus();

        assertThatThrownBy(()->status.toSending())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("Sending 상태에서 Active 상태로 변경 불가능하다.")
    void shouldRejectSendingChangeToActive() {
        ScheduleStatus status = new ScheduleActiveStatus();

        assertThatThrownBy(()->status.toActive())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("Sending 상태에서 Running 상태로 변경 불가능하다.")
    void shouldRejectSendingChangeToRunning() {
        ScheduleStatus status = new ScheduleSendingSchedule();
        assertThatThrownBy(() -> status.toRunning())
                .isInstanceOf(InvalidScheduleStatusException.class);

    }

    @Test
    @DisplayName("Sending 상태에서 INACTIVE 상태로 변경 불가능하다.")
    void shouldRejectSendingChangeToInActive() {
        ScheduleStatus status = new ScheduleSendingSchedule();
        assertThatThrownBy(() -> status.toInActive())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("Sending 상태에서 PAUSE 상태로 변경한다.")
    void shouldReturnPauseChangeSending() {
        ScheduleStatus status = new ScheduleSendingSchedule();
        ScheduleStatus toStatus = status.toPause();
        assertThat(toStatus.currentScheduleStatus()).isEqualTo(ScheduleStatusEnum.PAUSE);
    }

    @Test
    @DisplayName("Sending 상태에서 STOP 상태로 변경한다.")
    void shouldReturnStopChangeSending() {
        ScheduleStatus status = new ScheduleSendingSchedule();
        ScheduleStatus toStatus = status.toStop();
        assertThat(toStatus.currentScheduleStatus()).isEqualTo(ScheduleStatusEnum.STOP);
    }

    @Test
    @DisplayName("Sending 상태에서 COMPLETED 상태로 변경한다.")
    void shouldReturnCompletedChangeSending() {
        ScheduleStatus status = new ScheduleSendingSchedule();
        ScheduleStatus toStatus = status.toCompleted();
        assertThat(toStatus.currentScheduleStatus()).isEqualTo(ScheduleStatusEnum.COMPLETED);
    }
}