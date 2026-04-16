package com.ums.schedule.domain.schedule.status;

import com.ums.schedule.domain.schedule.exception.InvalidScheduleStatusException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleStatusTest {
    @Test
    @DisplayName("스케쥴 상태가 RUNNING 상태가 아니면 익셉션이 발생한다.")
    void shouldThrowException_whenScheduleStatusIsNotRunning() {
        ScheduleStatus status = new ScheduleActiveStatus();

        Assertions.assertThatThrownBy(() -> status.canUse())
                .isInstanceOf(InvalidScheduleStatusException.class);
    }

}