package com.ums.schedule.intergration.schedule_status;

import com.ums.schedule.code.schedule.ScheduleStatusEnum;
import com.ums.schedule.domain.schedule.exception.InvalidScheduleStatusException;
import com.ums.schedule.domain.schedule.status.ScheduleActiveStatus;
import com.ums.schedule.domain.schedule.status.ScheduleInActiveStatus;
import com.ums.schedule.domain.schedule.status.ScheduleStatus;
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