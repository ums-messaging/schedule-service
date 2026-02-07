package com.ums.schedule.domain.schedule_status;

import com.ums.schedule.common.code.ScheduleStatusEnum;
import com.ums.schedule.domain.exception.InvalidScheduleStatusException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ScheduleInActiveStatusTest {
    @Test
    @DisplayName("InActive 상태에서 Active 상태로 변경 가능하다.")
    void shouldReturnInactiveToActive() {
        ScheduleStatus status = new ScheduleInActiveStatus();
        ScheduleActiveStatus toStatus = status.toActive();
        assertThat(toStatus.currentScheduleStatus())
                .isEqualTo(ScheduleStatusEnum.ACTIVE);
    }

    @Test
    @DisplayName("InActive 상태에서 Running 상태로 변경 불가능하다.")
    void shouldRejectInActiveChangeToRunning() {
        ScheduleStatus status = new ScheduleInActiveStatus();
        assertThatThrownBy(() -> status.toRunning())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("InActive 상태에서 Sending 상태로 변경 불가능하다.")
    void shouldRejectInActiveChangeToSending() {
        ScheduleStatus status = new ScheduleInActiveStatus();
        assertThatThrownBy(() -> status.toSending())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("InActive 상태에서 Completed 상태로 변경 불가능하다.")
    void shouldRejectInActiveChangeToCompleted() {
        ScheduleStatus status = new ScheduleInActiveStatus();
        assertThatThrownBy(() -> status.toCompleted())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("InActive 상태에서 Pause 상태로 변경 불가능하다.")
    void shouldRejectInActiveChangeToPause() {
        ScheduleStatus status = new ScheduleInActiveStatus();
        assertThatThrownBy(()->status.toPause())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("InActive 상태에서 Stop 상태로 변경 불가능하다.")
    void shouldRejectInActiveChangeToStop() {
        ScheduleStatus status = new ScheduleInActiveStatus();
        assertThatThrownBy(()->status.toStop())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("InActive 상태에서 InActive 상태로 변경 불가능하다.")
    void shouldRejectInActiveChangeToInActive() {
        ScheduleStatus status = new ScheduleInActiveStatus();
        assertThatThrownBy(() -> status.toInActive())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }
}