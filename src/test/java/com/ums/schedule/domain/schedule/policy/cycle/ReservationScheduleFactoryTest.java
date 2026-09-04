package com.ums.schedule.domain.schedule.policy.cycle;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.schedule.CycleCd;
import com.ums.schedule.common.code.schedule.ScheduleType;
import com.ums.schedule.application.schedule.factory.schedule_policy.ReservationScheduleFactory;
import com.ums.schedule.application.schedule.factory.schedule_policy.SchedulePolicyFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationScheduleFactoryTest {
    @Test
    @DisplayName("ScheduleType이 RESERVATION이면, TRUE를 반환한다.")
    void shouldReturnTrue_whenScheduleTypeIsReservation() {
        SchedulePolicyFactory factory = new ReservationScheduleFactory();

        boolean result = factory.supports(EnumMapperValue.fromEnumMapperType(ScheduleType.RESERVATION));

        assertThat(result).isTrue();
    }
    @Test
    @DisplayName("예약 스케쥴의 경우 CycleCd는 ONCE여야 한다.")
    void shouldCycleCdOnceScheduleTypeReservation() {
        // given
        SchedulePolicyFactory factory = new ReservationScheduleFactory();
        LocalDateTime now = LocalDateTime.now().plusDays(1).withSecond(0).withNano(0);
        String format = now.format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));

        // when
        ScheduleCyclePolicy result = factory.create(null, format);

        // then
        assertThat(result.getScheduleType()).isEqualTo(ScheduleType.RESERVATION);
        assertThat(result.getCycleCd()).isEqualTo(CycleCd.ONCE);
        assertThat(result.getPolicyValue().getCycleValue()).isEqualTo(format);
    }

}