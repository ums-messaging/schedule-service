package com.ums.schedule.domain.schedule.status;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ScheduleStatusTest {
    @Test
    @DisplayName("스케쥴 상태가 RUNNING 상태가 아니면 False를 반환한다.")
    void shouldReturnFalse_whenScheduleStatusIsNotRunning() {
        ScheduleStatus status = new ScheduleActiveStatus();
        boolean expect = status.isRunning();

        assertThat(expect).isFalse();
    }

    @Test
    @DisplayName("스케쥴 상태가 RUNNING 상태이면 True를 반환한다.")
    void shouldReturnTrue_whenScheduleStatusIsRunning() {
        ScheduleStatus status = new ScheduleRunningStatus();
        boolean expect = status.isRunning();

        assertThat(expect).isTrue();
    }
}