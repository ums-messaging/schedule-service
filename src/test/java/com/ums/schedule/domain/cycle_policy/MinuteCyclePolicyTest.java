package com.ums.schedule.domain.cycle_policy;

import com.ums.schedule.common.code.CycleCdEnum;
import com.ums.schedule.domain.exception.InvalidCycleValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MinuteCyclePolicyTest {

    @Test
    @DisplayName("분 주기의 스케쥴 주기 코드는 MINUTE 이어야 한다.")
    void shouldReturnCycleCdMinute() {
        ScheduleCyclePolicy policy = new MinuteCyclePolicy(30);
        assertThat(policy.getCycleCd()).isEqualTo(CycleCdEnum.MINUTE);
    }

    @Test
    @DisplayName("분 주기 스케쥴 주기 값은 1~59분 단위여야 한다.")
    void shouldRejectInvalidCycleValue() {
        assertThatThrownBy(() -> new MinuteCyclePolicy(60))
                .isInstanceOf(InvalidCycleValueException.class);
    }

}