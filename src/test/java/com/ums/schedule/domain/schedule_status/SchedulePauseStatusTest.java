package com.ums.schedule.domain.schedule_status;

import com.ums.schedule.common.code.ScheduleStatusEnum;
import com.ums.schedule.domain.exception.InvalidScheduleStatusException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SchedulePauseStatusTest {
    @Test
    @DisplayName("Pause 상태에서 Pause 상태로 변경 불가능하다.")
    void shouldRejectPauseChangeToPause() {
        ScheduleStatus status = new SchedulePauseStatus();
        assertThatThrownBy(() -> status.toPause())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("Pause 상태에서 Active 상태로 변경 불가능하다.")
    void shouldRejectPauseChangeToActive() {
        ScheduleStatus status = new SchedulePauseStatus();
        assertThatThrownBy(() -> status.toActive())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("Pause 상태에서 Running 상태로 변경한다.")
    void shouldReturnPauseToRunning() {
        ScheduleStatus status = new SchedulePauseStatus();
        ScheduleStatus toStatus = status.toRunning();
        assertThat(toStatus.currentScheduleStatus()).isEqualTo(ScheduleStatusEnum.RUNNING);
    }

    @Test
    @DisplayName("Pause 상태에서 Sending 상태로 변경 불가능하다.")
    void shouldRejectPauseToSending(){
        ScheduleStatus status = new SchedulePauseStatus();

        assertThatThrownBy(() -> status.toSending())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }


    @Test
    @DisplayName("Pause 상태에서 Stop 상태로 변경한다.")
    void shouldReturnStopChangePause() {
        ScheduleStatus status = new SchedulePauseStatus();
        ScheduleStatus toStatus = status.toStop();
        assertThat(toStatus.currentScheduleStatus())
                .isEqualTo(ScheduleStatusEnum.STOP);
    }

    @Test
    @DisplayName("Pause 상태에서 COMPLETED 상태로 변경 불가능하다.")
    void shouldRejectPauseToCompleted() {
        ScheduleStatus status = new SchedulePauseStatus();
        assertThatThrownBy(() -> status.toCompleted())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("Pause 상태에서 INACTIVE 상태로 변경 불가능하다. ")
    void shouldRejectPauseToInactive() {
        ScheduleStatus status = new SchedulePauseStatus();
        assertThatThrownBy(() -> status.toInActive())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }


}