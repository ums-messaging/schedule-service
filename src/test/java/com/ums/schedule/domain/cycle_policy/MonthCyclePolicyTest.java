package com.ums.schedule.domain.cycle_policy;

import com.ums.schedule.common.code.CycleCdEnum;
import com.ums.schedule.domain.exception.InvalidCycleValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MonthCyclePolicyTest {
    @Test
    @DisplayName("월 주기 스케쥴의 경우 cycleCd는 MONTH여야 한다.")
    void shouldReturnCycleCdMonth() {
        ScheduleCyclePolicy policy = new MonthCyclePolicy(11);

        assertThat(policy.getCycleCd()).isEqualTo(CycleCdEnum.MONTH);
    }
    @Test
    @DisplayName("월 주기 스케쥴의 경우 cycleValue는 1~12까지 입력할 수 있다.")
    void shouldMonthCycleRejectCycleValue() {
        // Given
        int month = 13;

        // When, Then
        assertThatThrownBy(() -> new MonthCyclePolicy(month))
                .isInstanceOf(InvalidCycleValueException.class);
    }
}