package com.ums.schedule.domain.schedule.policy.cycle;

import com.ums.schedule.domain.schedule.code.CycleCdEnum;
import com.ums.schedule.application.schedule.factory.cycle_policy.CyclePolicy;
import com.ums.schedule.application.schedule.factory.cycle_policy.HourCyclePolicyFactory;
import com.ums.schedule.domain.schedule.exception.InvalidCycleValueException;
import com.ums.schedule.common.exception.validation.InvalidNumberFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HourCyclePolicyTest {
    @Test
    @DisplayName("시간 주기의 스케쥴의 주키 코드는 HOUR이어야 한다.")
    void shouldReturnCycleCdHour() {
        CyclePolicy cyclePolicy = new HourCyclePolicyFactory();
        SchedulePolicyValue policy = cyclePolicy.create(1);
        assertThat(policy.getCycleCdEnum()).isEqualTo(CycleCdEnum.HOUR);
    }
    @Test
    @DisplayName("시간 주기의 스케쥴의 주기 값은 1~12의 시간 단위어야 한다.")
    void shouldReturnCycleValueHour() {
        CyclePolicy cyclePolicy = new HourCyclePolicyFactory();

        assertThatThrownBy(() -> cyclePolicy.create(13))
                .isInstanceOf(InvalidCycleValueException.class);
    }

    @Test
    @DisplayName("숫자가 아닌 값이 입력되면 에러가 발생한다.")
    void shouldThrowException_whenCycleValueIsNotNumber() {
        CyclePolicy policy = new HourCyclePolicyFactory();
        assertThatThrownBy(() -> policy.create("number"))
                .isInstanceOf(InvalidNumberFormatException.class)
                .hasMessage(InvalidNumberFormatException.ofCycleValue().getMessage());
    }
}