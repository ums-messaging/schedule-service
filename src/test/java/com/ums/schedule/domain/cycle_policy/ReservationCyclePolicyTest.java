package com.ums.schedule.domain.cycle_policy;

import com.ums.schedule.common.code.CycleCdEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationCyclePolicyTest {
    @Test
    @DisplayName("예약 스케쥴의 경우 CycleCd는 ONCE여야 한다.")
    void shouldCycleCdOnceScheduleTypeReservation() {
        // given
        LocalDateTime now = LocalDateTime.now().plusDays(1);

        // when
        ScheduleCyclePolicy policy = new ReservationCyclePolicy(now);

        // then
        assertThat(policy.getCycleCd()).isEqualTo(CycleCdEnum.ONCE);
    }

}