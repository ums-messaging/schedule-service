package com.ums.schedule.domain.schedule_status;

import com.ums.schedule.common.code.ScheduleStatusEnum;
import com.ums.schedule.domain.exception.InvalidScheduleStatusException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ScheduleStopStatusTest {
    @Test
    @DisplayName("Stop 상태에서 Active 상태로 변경 불가능하다.")
    void shouldRejectChangeToActive() {
        ScheduleStopStatus status = new ScheduleStopStatus();
        assertThatThrownBy(() -> status.toActive())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("Stop 상태에서 Running 상태로 변경 불가능하다.")
    void shouldRejectChangeToRunning() {
        ScheduleStopStatus status = new ScheduleStopStatus();
        assertThatThrownBy(() -> status.toRunning())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("Stop 상태에서 Sending 상태로 변경 불가능하다.")
    void shouldRejectChangeToSending() {
        ScheduleStopStatus status = new ScheduleStopStatus();
        assertThatThrownBy(() -> status.toSending())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("Stop 상태에서 Completed 상태로 변경 가능하다.")
    void shouldReturnStopToCompleted() {
        ScheduleStopStatus status = new ScheduleStopStatus();
        ScheduleStatus toStatus = status.toCompleted();
        assertThat(toStatus.currentScheduleStatus())
                .isEqualTo(ScheduleStatusEnum.COMPLETED);
    }

    @Test
    @DisplayName("Stop 상태에서 Pause 상태로 변경 불가능하다.")
    void shouldRejectStopChangeToPause() {
        ScheduleStopStatus status = new ScheduleStopStatus();
        assertThatThrownBy(() -> status.toPause())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("Stop 상태에서 Stop 상태로 변경 불가능하다.")
    void shouldRejectStopChangeToStop() {
        ScheduleStopStatus status = new ScheduleStopStatus();
        assertThatThrownBy(() -> status.toStop())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("Stop 상태에서 Inactvie 상태로 변경 불가능하다.")
    void shouldRejectStopChangeToInactive(){
        ScheduleStatus status = new ScheduleStopStatus();
        assertThatThrownBy(() -> status.toInActive())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }
}