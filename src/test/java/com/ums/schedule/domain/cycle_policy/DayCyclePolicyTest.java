package com.ums.schedule.domain.cycle_policy;

import com.ums.schedule.common.code.CycleCdEnum;
import com.ums.schedule.domain.exception.InvalidCycleValueException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class DayCyclePolicyTest {
    @Test
    @DisplayName("일 주기의 스케쥴의 주기 코드는 DAY여야 한다.")
    void shouldReturnCycleCdDay() {
        ScheduleCyclePolicy policy = new DayCyclePolicy(30);
        assertThat(policy.getCycleCd()).isEqualTo(CycleCdEnum.DAY);
    }

    @Test
    @DisplayName("일 주기의 스케쥴의 주기 값은 1~31 입력 값만 가능하다.")
    void shouldRejectCycleValueDay () {
        assertThatThrownBy(() -> new DayCyclePolicy(32))
                .isInstanceOf(InvalidCycleValueException.class);
    }

}