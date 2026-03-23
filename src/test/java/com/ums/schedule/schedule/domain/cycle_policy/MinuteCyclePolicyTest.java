package com.ums.schedule.schedule.domain.cycle_policy;

import com.ums.schedule.schedule.application.factory.cycle_policy.CyclePolicy;
import com.ums.schedule.schedule.application.factory.cycle_policy.HourCyclePolicyFactory;
import com.ums.schedule.schedule.application.factory.cycle_policy.MinuteCyclePolicyFactory;
import com.ums.schedule.schedule.code.CycleCdEnum;
import com.ums.schedule.schedule.domain.exception.InvalidCycleValueException;
import com.ums.schedule.schedule.domain.exception.InvalidNumberFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MinuteCyclePolicyTest {

    @Test
    @DisplayName("분 주기의 스케쥴 주기 코드는 MINUTE 이어야 한다.")
    void shouldReturnCycleCdMinute() {
        CyclePolicy cyclePolicy = new MinuteCyclePolicyFactory();
        SchedulePolicyValue policy = cyclePolicy.create(30);
        assertThat(policy.getCycleCdEnum()).isEqualTo(CycleCdEnum.MINUTE);
    }

    @Test
    @DisplayName("분 주기 스케쥴 주기 값은 1~59분 단위여야 한다.")
    void shouldRejectInvalidCycleValue() {
        CyclePolicy cyclePolicy = new MinuteCyclePolicyFactory();
        assertThatThrownBy(() ->  cyclePolicy.create(60))
                .isInstanceOf(InvalidCycleValueException.class);
    }
    @Test
    @DisplayName("숫자가 아닌 값이 입력되면 에러가 발생한다.")
    void shouldThrowException_whenCycleValueIsNotNumber() {
        CyclePolicy policy = new MinuteCyclePolicyFactory();
        assertThatThrownBy(() -> policy.create("number"))
                .isInstanceOf(InvalidNumberFormatException.class)
                .hasMessage(InvalidNumberFormatException.ofCycleValue().getMessage());
    }
}