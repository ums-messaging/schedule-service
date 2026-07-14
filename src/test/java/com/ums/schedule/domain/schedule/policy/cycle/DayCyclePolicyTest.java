package com.ums.schedule.domain.schedule.policy.cycle;

import com.ums.schedule.common.code.schedule.CycleCdEnum;
import com.ums.schedule.application.schedule.factory.cycle_policy.CyclePolicy;
import com.ums.schedule.application.schedule.factory.cycle_policy.DayCyclePolicyFactory;
import com.ums.schedule.domain.exception.schedule.InvalidCycleValueException;
import com.ums.schedule.common.exception.validation.InvalidNumberFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DayCyclePolicyTest {
    @Test
    @DisplayName("일 주기의 스케쥴의 주기 코드는 DAY여야 한다.")
    void shouldReturnCycleCdDay() {
        CyclePolicy policy = new DayCyclePolicyFactory();
        SchedulePolicyValue cycleValue = policy.create(30);

        assertThat(cycleValue.getCycleCdEnum()).isEqualTo(CycleCdEnum.DAY);
    }

    @Test
    @DisplayName("일 주기의 스케쥴의 주기 값은 1~31 입력 값만 가능하다.")
    void shouldRejectCycleValueDay () {
        CyclePolicy policy = new DayCyclePolicyFactory();
        assertThatThrownBy(() -> policy.create(32))
                .isInstanceOf(InvalidCycleValueException.class)
                .hasMessage(InvalidCycleValueException.toDay().getMessage());
    }
    @Test
    @DisplayName("숫자가 아닌 값이 입력되면 에러가 발생한다.")
    void shouldThrowException_whenCycleValueIsNotNumber() {
        CyclePolicy policy = new DayCyclePolicyFactory();
        assertThatThrownBy(() -> policy.create("number"))
                .isInstanceOf(InvalidNumberFormatException.class)
                .hasMessage(InvalidNumberFormatException.ofCycleValue().getMessage());
    }
}