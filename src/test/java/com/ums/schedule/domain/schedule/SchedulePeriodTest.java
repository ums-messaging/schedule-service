package com.ums.schedule.domain.schedule;

import com.ums.schedule.domain.schedule.period.SchedulePeriod;
import com.ums.schedule.domain.schedule.exception.InvalidSchedulePeriodException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SchedulePeriodTest {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Test
    @DisplayName("시작 날짜가 빈 값이면 오류가 발생한다.")
    void shouldThrowException_whenScheduleStartAtIsEmpty() {
        String scheduleEndAt = LocalDate.now().plusMonths(1).format(formatter);

        InvalidSchedulePeriodException expect = InvalidSchedulePeriodException.requiredSchedulePeriod();

        assertThatThrownBy(() -> SchedulePeriod.of("", scheduleEndAt))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("시작 날짜가 유효하지 않은 날짜 형식이면 오류가 발생한다.")
    void shouldThrowException_whenScheduleStartAtIsInvalid() {
        String scheduleStartAt = "Invalid Date";
        String scheduleEndAt = LocalDate.now().plusMonths(1).format(formatter);

        InvalidSchedulePeriodException expect = InvalidSchedulePeriodException.invalidFormat();
        assertThatThrownBy(() -> SchedulePeriod.of(scheduleStartAt, scheduleEndAt))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("종료 날짜가 빈 값이면 오류가 발생한다.")
    void shouldThrowException_whenScheduleEndAtIsEmpty() {
        String scheduleStartAt = LocalDate.now().plusMonths(1).format(formatter);

        InvalidSchedulePeriodException expect = InvalidSchedulePeriodException.requiredSchedulePeriod();

        assertThatThrownBy(() -> SchedulePeriod.of(scheduleStartAt, " "))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("종료 날짜가 유효하지 않은 날짜 형식으면 오류가 발생한다.")
    void shouldThrowException_whenScheduleEndAtIsInvalid() {
        String scheduleStartAt = LocalDate.now().plusMonths(1).format(formatter);
        String scheduleEndAt = "Invalid Date";

        InvalidSchedulePeriodException expect = InvalidSchedulePeriodException.invalidFormat();

        assertThatThrownBy(() -> SchedulePeriod.of(scheduleStartAt, scheduleEndAt))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("시작 날짜는 현재 날짜보다 이전일 수 없다.")
    void shouldRejectStartAtBeforeNow() {
        // given
        LocalDate scheduleStartAt = LocalDate.now().minusDays(1);
        LocalDate scheduleEndAt = scheduleStartAt.plusDays(1);

        // When, Then
        assertThatThrownBy(
                () -> SchedulePeriod.of(scheduleStartAt.format(formatter), scheduleEndAt.format(formatter)))
                .isInstanceOf(InvalidSchedulePeriodException.class);
    }

    @Test
    @DisplayName("종료 날짜는 시작 날짜 이후어야 한다.")
    void shouldRejectEndAtAfterStartAt() {
        LocalDate now = LocalDate.now();
        LocalDate scheduleStartAt = now;
        LocalDate scheduleEndAt = scheduleStartAt;

        assertThatThrownBy(
                () -> SchedulePeriod.of(scheduleStartAt.format(formatter), scheduleEndAt.format(formatter))
        ).isInstanceOf(InvalidSchedulePeriodException.class);
    }

    @Test
    @DisplayName("주어진 날짜가 스케쥴 시작 날짜보다 이전이면 false를 반환한다.")
    void shouldReturnFalse_whenCurrentDateDoesNotContainSchedulePeriod() {
        LocalDateTime scheduleStartAt = LocalDateTime.now().plusDays(2);
        LocalDateTime scheduleEndAt = scheduleStartAt.plusDays(1);
        LocalDateTime givenAt = scheduleStartAt.minusDays(1);

        SchedulePeriod schedulePeriod = SchedulePeriod.of(scheduleStartAt.format(formatter), scheduleEndAt.format(formatter));
        boolean expect = schedulePeriod.isScheduleWindow(givenAt);

        assertThat(expect).isFalse();
    }

    @Test
    @DisplayName("주어진 날짜가 스케쥴 종료 날짜보다 이후이면 false를 반환한다.")
    void shouldReturnFalse_whenCurrentDateTimeDoesNotContainSchedulePeriod() {
        LocalDateTime scheduleStartAt = LocalDateTime.now().plusDays(2);
        LocalDateTime scheduleEndAt = scheduleStartAt.plusDays(1);
        LocalDateTime givenAt = scheduleEndAt.plusDays(1);

        SchedulePeriod schedulePeriod = SchedulePeriod.of(scheduleStartAt.format(formatter), scheduleEndAt.format(formatter));
        boolean expect = schedulePeriod.isScheduleWindow(givenAt);

        assertThat(expect).isFalse();
    }

    @Test
    @DisplayName("현재 시각이 스케쥴 기간에 해당하면 true를 반환한다.")
    void shouldReturnTrue_whenCurrentDateTimeDoesNotContainSchedulePeriod() {
        LocalDate scheduleStartAt = LocalDate.now();
        LocalDate scheduleEndAt = scheduleStartAt.plusDays(1);

        SchedulePeriod schedulePeriod = SchedulePeriod.of(scheduleStartAt.format(formatter), scheduleEndAt.format(formatter));
        boolean expect = schedulePeriod.isScheduleWindow(LocalDateTime.now());

        assertThat(expect).isTrue();
    }
}
