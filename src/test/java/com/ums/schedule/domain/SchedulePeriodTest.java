package com.ums.schedule.domain;

import com.ums.schedule.domain.schedule.period.SchedulePeriod;
import com.ums.schedule.domain.schedule.exception.InvalidSchedulePeriodException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SchedulePeriodTest {

    @Test
    @DisplayName("시작 날짜는 현재 날짜보다 이전일 수 없다.")
    void shouldRejectStartAtBeforeNow() {
        // given
        LocalDateTime scheduleStartAt = LocalDateTime.now().minusDays(1);
        LocalDateTime scheduleEndAt = scheduleStartAt.plusDays(1);

        // When, Then
        assertThatThrownBy(
                () -> SchedulePeriod.of(scheduleStartAt, scheduleEndAt))
                .isInstanceOf(InvalidSchedulePeriodException.class);
    }

    @Test
    @DisplayName("종료 날짜는 시작 날짜 이후어야 한다.")
    void shouldRejectEndAtAfterStartAt() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime scheduleStartAt = now.plusMinutes(1);
        LocalDateTime scheduleEndAt = scheduleStartAt.plusHours(3);

        assertThatThrownBy(
                () -> SchedulePeriod.of(scheduleStartAt, scheduleEndAt)
        ).isInstanceOf(InvalidSchedulePeriodException.class);
    }

    @Test
    @DisplayName("현재 시각이 스케쥴 기간에 해당하지 않으면 익셉션이 발생한다.")
    void shouldThrowException_whenCurrentDateTimeDoesNotContainSchedulePeriod() {
        LocalDateTime scheduleStartAt = LocalDateTime.now().minusDays(2);
        LocalDateTime scheduleEndAt = scheduleStartAt.plusDays(1);

        SchedulePeriod schedulePeriod = SchedulePeriod.of(scheduleStartAt, scheduleEndAt);

        // When, Then
        assertThatThrownBy(
                () -> schedulePeriod.validateScheduleWindow())
                .isInstanceOf(InvalidSchedulePeriodException.class);
    }
}
