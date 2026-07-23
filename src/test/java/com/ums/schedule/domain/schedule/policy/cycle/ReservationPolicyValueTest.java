package com.ums.schedule.domain.schedule.policy.cycle;

import com.ums.schedule.common.code.api.ScheduleErrorCode;
import com.ums.schedule.common.code.schedule.CycleCd;
import com.ums.schedule.common.code.schedule.ScheduleType;
import com.ums.schedule.common.exception.validate.InvalidDateFormatException;
import com.ums.schedule.domain.schedule.exception.InvalidScheduleCyclePolicyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationPolicyValueTest {
    @Test
    @DisplayName("예약 시간이 오늘 기준으로 하루 전이면, 에러가 발생한다.")
    void shouldThrowException_whenReservationDateIsBeforeTodayAfterOneHour() {
        // given
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        String format = now.format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));

        // when
        assertThatThrownBy(() -> ReservationPolicyValue.of(format))
                .isInstanceOf(InvalidScheduleCyclePolicyException.class)
                .hasMessage(InvalidScheduleCyclePolicyException.of(ScheduleErrorCode.INVALID_RESERVATION_DATE).getMessage());
    }

    @Test
    @DisplayName("예약 시간이 날짜 포맷이 아닌 경우 에러가 발생한다.")
    void shouldThrowException_whenReservationDateFormatIsInvalid() {
        String format = "yyyyMMdd";

        // when
        assertThatThrownBy(() -> ReservationPolicyValue.of(format))
                .isInstanceOf(InvalidDateFormatException.class)
                .hasMessage(InvalidDateFormatException.of("RESERVATION_DATE").getMessage());
    }

    @Test
    @DisplayName("예약 시간을 입력하면, getCycleValue()는 입력 포맷으로 리턴된다.")
    void shouldReturnCycleValueIsStringFormat_whenReservationDateInput() {
        LocalDateTime now = LocalDateTime.now().plusHours(1).withSecond(0).withNano(0);
        String format = now.format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));

        // when
        ReservationPolicyValue result = ReservationPolicyValue.of(format);

        assertThat(result.getCycleValue()).isEqualTo(format);
        assertThat(result.getScheduleType()).isEqualTo(ScheduleType.RESERVATION);
        assertThat(result.getCycleCdEnum()).isEqualTo(CycleCd.ONCE);

    }
}