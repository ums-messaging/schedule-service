package com.ums.schedule.domain.schedule.policy.cycle;

import com.ums.schedule.common.code.api.ScheduleErrorCode;
import com.ums.schedule.common.code.schedule.CycleCd;
import com.ums.schedule.application.schedule.factory.cycle_policy.CyclePolicy;
import com.ums.schedule.application.schedule.factory.cycle_policy.MonthCyclePolicyFactory;
import com.ums.schedule.domain.schedule.exception.InvalidScheduleCyclePolicyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MonthCyclePolicyTest {
    @Test
    @DisplayName("월 주기 스케쥴의 경우 cycleCd는 MONTH여야 한다.")
    void shouldReturnCycleCdMonth() {
        CyclePolicy cyclePolicy = new MonthCyclePolicyFactory();
        SchedulePolicyValue policy = cyclePolicy.create(11);

        assertThat(policy.getCycleCdEnum()).isEqualTo(CycleCd.MONTH);
    }
    @Test
    @DisplayName("월 주기 스케쥴의 경우 cycleValue는 1~12까지 입력할 수 있다.")
    void shouldMonthCycleRejectCycleValue() {
        // Given
        CyclePolicy cyclePolicy = new MonthCyclePolicyFactory();

        InvalidScheduleCyclePolicyException expect = InvalidScheduleCyclePolicyException.of(ScheduleErrorCode.CYCLE_VALUE_NOT_MONTH);

        // When, Then
        assertThatThrownBy(() -> cyclePolicy.create(13))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage())
        ;
    }
    @Test
    @DisplayName("숫자가 아닌 값이 입력되면 에러가 발생한다.")
    void shouldThrowException_whenCycleValueIsNotNumber() {
        CyclePolicy policy = new MonthCyclePolicyFactory();

        InvalidScheduleCyclePolicyException expect = InvalidScheduleCyclePolicyException.of(ScheduleErrorCode.CYCLE_VALUE_NOT_MONTH);

        assertThatThrownBy(() -> policy.create("number"))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }
}