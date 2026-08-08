package com.ums.schedule.domain.schedule.policy.cycle;

import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.schedule.ScheduleCode;
import com.ums.schedule.common.code.schedule.ScheduleType;
import com.ums.schedule.application.schedule.factory.cycle_policy.DayCyclePolicyFactory;
import com.ums.schedule.application.schedule.factory.cycle_policy.HourCyclePolicyFactory;
import com.ums.schedule.application.schedule.factory.cycle_policy.MinuteCyclePolicyFactory;
import com.ums.schedule.application.schedule.factory.cycle_policy.MonthCyclePolicyFactory;
import com.ums.schedule.application.schedule.factory.schedule_policy.CycleScheduleFactory;
import com.ums.schedule.application.schedule.factory.schedule_policy.SchedulePolicyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.ums.schedule.common.code.mapper.EnumMapperValue.fromEnumMapperType;
import static com.ums.schedule.common.code.schedule.CycleCd.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CycleScheduleFactoryTest {
    @Mock private EnumMapperFactory enumMapperFactory;
    @Mock private MonthCyclePolicyFactory monthFactory;
    @Mock private DayCyclePolicyFactory dayFactory;
    @Mock private HourCyclePolicyFactory hourFactory;
    @Mock private MinuteCyclePolicyFactory minuteFactory;

    private SchedulePolicyFactory factory;

    @BeforeEach
    void setUp() {
        factory = new CycleScheduleFactory(
                enumMapperFactory,
                List.of(monthFactory, dayFactory, hourFactory, minuteFactory)
        );
    }

    @Test
    @DisplayName("ScheduleType이 CYCLE이면 TRUE를 반환한다.")
    void shouldReturnTrue_whenScheduleTypeIsCycle() {
        boolean result = factory.supports(fromEnumMapperType(ScheduleType.CYCLE));
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("CYCLE_CD가 MONTH이면 MonthCyclePolicyFactory가 실행된다.")
    void shouldExecuteMonthCyclePolicyFactory_whenCycleCdIsMonth() {
        CyclePolicyValue expected = CyclePolicyValue.of(MONTH, 3);
        when(enumMapperFactory.findEnumMapperValue(any(), any())).thenReturn(fromEnumMapperType(MONTH));
        when(monthFactory.supports(any())).thenReturn(true);
        when(monthFactory.create(anyInt())).thenReturn(expected);

        factory.create(MONTH.value(), "3");

        verify(enumMapperFactory).findEnumMapperValue(ScheduleCode.CYCLE_CD, MONTH.value());
        verify(monthFactory).create(3);
    }

    @Test
    @DisplayName("CYCLE_CD가 MONTH이면 SchedulePolicy의 ScheduleType과 CycleCd는 CYCLE과 MONTH이다.")
    void shouldReturnScheduleTypeIsCycleAndCycleCdIsCycle_whenCycleCdIsMonth() {
        CyclePolicyValue expected = CyclePolicyValue.of(MONTH, 3);
        when(enumMapperFactory.findEnumMapperValue(any(), any())).thenReturn(fromEnumMapperType(MONTH));
        when(monthFactory.supports(any())).thenReturn(true);
        when(monthFactory.create(anyInt())).thenReturn(expected);

        ScheduleCyclePolicy result = factory.create(MONTH.value(), "3");

        assertThat(result.getScheduleType()).isEqualTo(ScheduleType.CYCLE);
        assertThat(result.getCycleCd()).isEqualTo(MONTH);
        assertThat(result.getPolicyValue().getCycleValue()).isEqualTo("3");
    }

    @Test
    @DisplayName("CYCLE_CD가 DAY이면 SchedulePolicy의 ScheduleType과 CycleCd는 CYCLE과 DAY이다.")
    void shouldReturnScheduleTypeIsCycleAndCycleCdIsCycle_whenCycleCdIsDay() {
        CyclePolicyValue expected = CyclePolicyValue.of(DAY, 3);
        when(enumMapperFactory.findEnumMapperValue(any(), any())).thenReturn(fromEnumMapperType(DAY));
        when(monthFactory.supports(any())).thenReturn(true);
        when(monthFactory.create(anyInt())).thenReturn(expected);

        ScheduleCyclePolicy result = factory.create(DAY.value(), "3");

        assertThat(result.getScheduleType()).isEqualTo(ScheduleType.CYCLE);
        assertThat(result.getCycleCd()).isEqualTo(DAY);
        assertThat(result.getPolicyValue().getCycleValue()).isEqualTo("3");
    }

    @Test
    @DisplayName("CYCLE_CD가 MINUTE이면 SchedulePolicy의 ScheduleType과 CycleCd는 CYCLE과 MINUTE이다.")
    void shouldReturnScheduleTypeIsCycleAndCycleCdIsCycle_whenCycleCdIsMinute() {
        CyclePolicyValue expected = CyclePolicyValue.of(MINUTE, 3);
        when(enumMapperFactory.findEnumMapperValue(any(), any())).thenReturn(fromEnumMapperType(MINUTE));
        when(monthFactory.supports(any())).thenReturn(true);
        when(monthFactory.create(anyInt())).thenReturn(expected);

        ScheduleCyclePolicy result = factory.create(MINUTE.value(), "3");

        assertThat(result.getScheduleType()).isEqualTo(ScheduleType.CYCLE);
        assertThat(result.getCycleCd()).isEqualTo(MINUTE);
        assertThat(result.getPolicyValue().getCycleValue()).isEqualTo("3");
    }

    @Test
    @DisplayName("CYCLE_CD가 HOUR이면 SchedulePolicy의 ScheduleType과 CycleCd는 CYCLE과 HOUR이다.")
    void shouldReturnScheduleTypeIsCycleAndCycleCdIsCycle_whenCycleCdIsHour() {
        CyclePolicyValue expected = CyclePolicyValue.of(HOUR, 3);
        when(enumMapperFactory.findEnumMapperValue(any(), any())).thenReturn(fromEnumMapperType(HOUR));
        when(monthFactory.supports(any())).thenReturn(true);
        when(monthFactory.create(anyInt())).thenReturn(expected);

        ScheduleCyclePolicy result = factory.create(HOUR.value(), "3");

        assertThat(result.getScheduleType()).isEqualTo(ScheduleType.CYCLE);
        assertThat(result.getCycleCd()).isEqualTo(HOUR);
        assertThat(result.getPolicyValue().getCycleValue()).isEqualTo("3");
    }


}
