package com.ums.schedule.schedule.domain.schedule_policy;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.schedule.application.factory.schedule_policy.RealtimeScheduleFactory;
import com.ums.schedule.schedule.application.factory.schedule_policy.SchedulePolicyFactory;
import com.ums.schedule.schedule.code.CycleCdEnum;
import com.ums.schedule.schedule.code.ScheduleTypeEnum;
import com.ums.schedule.schedule.domain.cycle_policy.ScheduleCyclePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RealtimeScheduleFactoryTest {
    @Test
    @DisplayName("ScheduleType이 REALTIME이면 true를 반환한다.")
    void shouldReturnTrue_whenScheduleTypeIsRealtime() {
        SchedulePolicyFactory cyclePolicy = new RealtimeScheduleFactory();
        boolean result = cyclePolicy.supports(EnumMapperValue.fromEnumMapperType(ScheduleTypeEnum.REALTIME));
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("실시간 스케쥴의 경우 CycleCd는 ALWAYS여야 한다.")
    void shouldCycleCdAlwaysScheduleTypeRealtime() {
        // When
        SchedulePolicyFactory cyclePolicy = new RealtimeScheduleFactory();

        ScheduleCyclePolicy policy = cyclePolicy.create(null, null);

        // Then
        assertThat(policy.cycleCd()).isEqualTo(CycleCdEnum.ALWAYS);
        assertThat(policy.scheduleType()).isEqualTo(ScheduleTypeEnum.REALTIME);
    }
}