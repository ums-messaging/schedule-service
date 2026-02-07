package com.ums.schedule.domain.cycle_policy;

import com.ums.schedule.common.code.CycleCdEnum;
import com.ums.schedule.domain.exception.InvalidCycleValueException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class HourCyclePolicyTest {
    @Test
    @DisplayName("시간 주기의 스케쥴의 주키 코드는 HOUR이어야 한다.")
    void shouldReturnCycleCdHour() {
        ScheduleCyclePolicy policy = new HourCyclePolicy(1);
        assertThat(policy.getCycleCd()).isEqualTo(CycleCdEnum.HOUR);
    }
    @Test
    @DisplayName("시간 주기의 스케쥴의 주기 값은 1~12의 시간 단위어야 한다.")
    void shouldReturnCycleValueHour() {
        assertThatThrownBy(() -> new HourCyclePolicy(13))
                .isInstanceOf(InvalidCycleValueException.class);
    }

}