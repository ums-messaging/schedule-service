package com.ums.schedule.schedule.schedule_status;

import com.ums.schedule.schedule.domain.exception.InvalidScheduleStatusException;
import com.ums.schedule.schedule.code.ScheduleStatusEnum;
import com.ums.schedule.schedule.domain.status.ScheduleActiveStatus;
import com.ums.schedule.schedule.domain.status.ScheduleInActiveStatus;
import com.ums.schedule.schedule.domain.status.ScheduleStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    @DisplayName("InActive 상태에서 InActive 상태로 변경 불가능하다.")
    void shouldRejectInActiveChangeToInActive() {
        ScheduleStatus status = new ScheduleInActiveStatus();
        assertThatThrownBy(() -> status.toInActive())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }
}