package com.ums.schedule.intergration.schedule_status;

import com.ums.schedule.domain.schedule.exception.InvalidScheduleStatusException;
import com.ums.schedule.domain.schedule.status.ScheduleActiveStatus;
import com.ums.schedule.domain.schedule.status.ScheduleRunningStatus;
import com.ums.schedule.domain.schedule.status.ScheduleStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScheduleRunningTest {
    @Test
    @DisplayName("ACTIVE -> RUNNING")
    void shouldActiveChangeToRunning() {
        // Given
        ScheduleStatus status = new ScheduleActiveStatus();

        // When
        ScheduleStatus toStatus = status.toRunning();

        // Then
        assertThat(toStatus.currentScheduleStatus()).isEqualTo(RUNNING);
    }

    @Test
    @DisplayName("Running 상태에서 Running 상태로 변경할 수 없다.")
    void shouldRejectRunningChangeToRunning() {
        // Given
        ScheduleStatus status = new ScheduleRunningStatus();

        // Then
        assertThatThrownBy(() -> status.toRunning())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }


    @Test
    @DisplayName("Running 상태에서 ACTIVE 상태로 변경 할 수 있다.")
    void shouldReturnRunningToActive() {
        ScheduleStatus status = new ScheduleRunningStatus();

        ScheduleActiveStatus toStatus = status.toActive();

        assertThat(toStatus.currentScheduleStatus()).isEqualTo(ACTIVE);
    }

    @Test
    @DisplayName("Running 상태에서 INACTIVE 상태로 변경 불가하다.")
    void shouldRejectRunningToInactive() {
        ScheduleStatus status = new ScheduleRunningStatus();
        assertThatThrownBy(() -> status.toInActive())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

}