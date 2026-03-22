package com.ums.schedule.domain.cycle_policy;

import com.ums.schedule.common.code.CycleCdEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RealtimeCyclePolicyTest {
    @Test
    @DisplayName("실시간 스케쥴의 경우 CycleCd는 ALWAYS여야 한다.")
    void shouldCycleCdAlwaysScheduleTypeRealtime() {
        // When
        ScheduleCyclePolicy policy = new RealtimeCyclePolicy();

        // Then
        assertThat(policy.getCycleCd()).isEqualTo(CycleCdEnum.ALWAYS);
    }
}