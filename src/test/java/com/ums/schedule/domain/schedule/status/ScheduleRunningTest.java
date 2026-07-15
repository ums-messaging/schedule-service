package com.ums.schedule.domain.schedule.status;

import com.ums.schedule.common.code.schedule.ScheduleEvent;
import com.ums.schedule.domain.exception.schedule.InvalidScheduleStatusException;
import com.ums.schedule.common.converter.StatusState;
import com.ums.schedule.domain.schedule.state.ScheduleActiveStatus;
import com.ums.schedule.domain.schedule.state.ScheduleRunningStatus;
import com.ums.schedule.domain.schedule.state.ScheduleStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.ums.schedule.common.code.schedule.ScheduleStatus.ACTIVE;
import static com.ums.schedule.common.code.schedule.ScheduleStatus.RUNNING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScheduleRunningTest {
    @Test
    @DisplayName("ACTIVE -> RUNNING")
    void shouldActiveChangeToRunning() {
        // Given
        ScheduleStatus status = new ScheduleActiveStatus();

        // When
        StatusState toStatus = status.onEvent(ScheduleEvent.TO_RUNNING);

        // Then
        assertThat(toStatus.getCurrentCode()).isEqualTo(RUNNING);
    }

    @Test
    @DisplayName("Running 상태에서 Running 상태로 변경할 수 없다.")
    void shouldRejectRunningChangeToRunning() {
        // Given
        ScheduleStatus status = new ScheduleRunningStatus();

        // Then
        assertThatThrownBy(() -> status.onEvent(ScheduleEvent.TO_RUNNING))
                .isInstanceOf(InvalidScheduleStatusException.class);
    }


    @Test
    @DisplayName("Running 상태에서 ACTIVE 상태로 변경 할 수 있다.")
    void shouldReturnRunningToActive() {
        ScheduleStatus status = new ScheduleRunningStatus();

        StatusState toStatus = status.onEvent(ScheduleEvent.TO_ACTIVE);

        assertThat(toStatus.getCurrentCode()).isEqualTo(ACTIVE);
    }

    @Test
    @DisplayName("Running 상태에서 INACTIVE 상태로 변경 불가하다.")
    void shouldRejectRunningToInactive() {
        ScheduleStatus status = new ScheduleRunningStatus();
        assertThatThrownBy(() -> status.onEvent(ScheduleEvent.TO_INACTIVE))
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

}