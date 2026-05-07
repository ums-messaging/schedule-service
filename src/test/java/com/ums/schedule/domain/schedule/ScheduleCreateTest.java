package com.ums.schedule.domain.schedule;

import com.ums.schedule.application.schedule.dto.ScheduleCreateCommand;
import com.ums.schedule.code.schedule.ScheduleEventEnum;
import com.ums.schedule.domain.schedule.cycle_policy.ReservationPolicyValue;
import com.ums.schedule.domain.schedule.cycle_policy.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule.exception.InvalidCycleValueException;
import com.ums.schedule.domain.schedule.exception.ScheduleNameRequiredException;
import com.ums.schedule.domain.schedule.period.SchedulePeriod;
import com.ums.schedule.domain.schedule.period.SchedulePeriodTestBuilder;
import com.ums.schedule.domain.state.schedule.ScheduleActiveStatus;
import com.ums.schedule.domain.state.schedule.ScheduleInActiveStatus;
import com.ums.schedule.domain.state.schedule.ScheduleRunningStatus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScheduleCreateTest {
    @Nested
    @DisplayName("스케쥴 생성 시")
    class whenScheduleCreate {
        @Test
        @DisplayName("스케쥴 상태는 ACTIVE이어야 한다.")
        void shouldReturnScheduleStatusIsActive_whenScheduleCreate() {
            ScheduleCreateCommand command = ScheduleTestBuilder.builder().toCommand();
            ScheduleCyclePolicy policy = ScheduleCyclePolicy.realtimeOf();

            Schedule expect = Schedule.of(command, policy);

            assertThat(expect.getScheduleStatus()).isInstanceOf(ScheduleActiveStatus.class);
        }

        @Test
        @DisplayName("스케쥴 명이 빈 값일 떄, 익셉션이 발생한다.")
        void shouldThrowException_whenScheduleNameIsEmpty() {
            ScheduleCreateCommand command = ScheduleTestBuilder.builder().scheduleName("").toCommand();

            ScheduleNameRequiredException expect = ScheduleNameRequiredException.of();

            assertThatThrownBy(() -> Schedule.of(command, ScheduleCyclePolicy.realtimeOf()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
    }

    @Nested
    @DisplayName("스케쥴 상태")
    class ScheduleStatusTest {
        @Test
        @DisplayName("스케쥴 상태가 ACTIVE일 때 RUNNING으로 변경하면 status는 RUNNING이 반환된다.")
        void shouldChangeToRunning_whenScheduleStatusChangeRunning() {
            Schedule schedule = ScheduleTestBuilder.builder()
                    .status(new ScheduleActiveStatus())
                    .build();

            schedule.toStatus(ScheduleEventEnum.TO_RUNNING);

            assertThat(schedule.getScheduleStatus()).isInstanceOf(ScheduleRunningStatus.class);
        }

        @Test
        @DisplayName("스케쥴 상태가 RUNNING일 떄 ACTIVE로 변경하면 Status는 ACTIVE가 반환된다.")
        void shouldReturnStatusActive_whenCalltoActive() {
            Schedule schedule = ScheduleTestBuilder.builder()
                    .status(new ScheduleRunningStatus())
                    .build();

            //when
            schedule.toStatus(ScheduleEventEnum.TO_ACTIVE);

            assertThat(schedule.getScheduleStatus()).isInstanceOf(ScheduleActiveStatus.class);
        }

        @Test
        @DisplayName("스케쥴 상태가 ACTIVE일 때, InACTIVE로 변경하면 Status는 INACTIVE가 반환된다.")
        void shouldReturnStatusDeActive_whenCallToInActive(){
            Schedule schedule = ScheduleTestBuilder.builder().status(new ScheduleActiveStatus()).build();
            schedule.toStatus(ScheduleEventEnum.TO_INACTIVE);

            assertThat(schedule.getScheduleStatus()).isInstanceOf(ScheduleInActiveStatus.class);
        }
    }

    @Nested
    @DisplayName("예약 주기 스케쥴일 경우")
    class ScheduleTypeReservationTest {
        @Test
        @DisplayName("예약 날짜가 스케쥴 시작 날짜보다 이전이면, 오류가 발생한다.")
        void shouldThrowException_whenReservationDateBeforeAtScheduleStartAt() {
            LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            LocalDateTime endAt = LocalDateTime.now().plusMonths(1);
            LocalDateTime givenAt = startAt.minusDays(1);

            ScheduleCreateCommand command = givenSchedulePeriodToCommand(startAt, endAt);
            ScheduleCyclePolicy policy = givenReservationAt(givenAt);

            assertThatThrownBy(() -> Schedule.of(command, policy))
                    .isInstanceOf(InvalidCycleValueException.class)
                    .hasMessage(InvalidCycleValueException.compareToReservationDate().getMessage());
        }

        @Test
        @DisplayName("예약 날짜가 스케쥴 종료 날짜보다 이후이면, 오류가 발생한다.")
        void shouldThrowException_whenReservationDateAfterAtScheduleEndAt() {
            LocalDateTime startAt = LocalDateTime.now();
            LocalDateTime endAt = startAt.plusMonths(1);
            LocalDateTime givenAt = endAt.plusDays(1);

            ScheduleCreateCommand command = givenSchedulePeriodToCommand(startAt, endAt);
            ScheduleCyclePolicy policy = givenReservationAt(givenAt);

            assertThatThrownBy(() -> Schedule.of(command, policy))
                    .isInstanceOf(InvalidCycleValueException.class)
                    .hasMessage(InvalidCycleValueException.compareToReservationDate().getMessage());
        }

        @Test
        @DisplayName("예약 날짜가 스케쥴 기간 안에 포함되면, ScheduleCyclePolicy와 SchedulePeriod는 NULL이 아니다.")
        void shouldReturnSchedulePeriodAndCyclePolicyIsNotNull_whenReservationAtContainsSchedulePerios() {
            LocalDateTime startAt = LocalDateTime.now().plusDays(1);
            LocalDateTime endAt = startAt.plusMonths(1);
            LocalDateTime givenAt = startAt.plusDays(1);

            ScheduleCreateCommand command = givenSchedulePeriodToCommand(startAt, endAt);
            ScheduleCyclePolicy policy = givenReservationAt(givenAt);

            Schedule result = Schedule.of(command, policy);

            assertThat(result.getCyclePolicy()).isNotNull();
            assertThat(result.getSchedulePeriod()).isNotNull();
        }
    }

    @Test
    @DisplayName("스케쥴 상태가 Running이 아니면, false를 반환한다.")
    void shouldReturnFalse_whenScheduleStatsIsNotRunning() {
        ScheduleCreateCommand command = givenSchedulePeriodToCommand(LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
        ScheduleCyclePolicy policy = ScheduleCyclePolicy.realtimeOf();

        Schedule schedule = Schedule.of(command, policy);
        boolean expect = schedule.availableSchedulePeriodAndStatus();

        assertThat(expect).isFalse();
    }

    @Test
    @DisplayName("현재 날짜가 스케쥴 기간 안에 포함되지 않으면 false를 반환한다.")
    void shouldReturnFalse_whenCurrentDateContainsInSchedulePeriod() {
        ScheduleCreateCommand command = givenSchedulePeriodToCommand(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusMonths(1));

        ScheduleCyclePolicy policy = ScheduleCyclePolicy.realtimeOf();

        Schedule schedule = Schedule.of(command, policy);
        schedule.toStatus(ScheduleEventEnum.TO_RUNNING);

        boolean expect = schedule.availableSchedulePeriodAndStatus();

        assertThat(expect).isFalse();
    }


    @Test
    @DisplayName("현재 날짜가 스케쥴 기간에 포함되고, 상태가 RUNNING이면 TRUE를 반환한다.")
    void shouldReturnTrue_whenCurrentDateContainsInSchedulePeriodAndStatusIsRunning() {
        ScheduleCreateCommand command = ScheduleTestBuilder.builder().toCommand();
        ScheduleCyclePolicy policy = ScheduleCyclePolicy.realtimeOf();

        Schedule schedule = Schedule.of(command, policy);
        schedule.toStatus(ScheduleEventEnum.TO_RUNNING);

        boolean expect = schedule.availableSchedulePeriodAndStatus();

        assertThat(expect).isTrue();
    }

    private ScheduleCreateCommand givenSchedulePeriodToCommand(LocalDateTime startAt, LocalDateTime endAt) {
        SchedulePeriod period = SchedulePeriodTestBuilder.builder()
                .scheduleStartAt(startAt.toLocalDate())
                .scheduleEndAt(endAt.toLocalDate())
                .build();
        ScheduleCreateCommand command = ScheduleTestBuilder.builder()
                .schedulePeriod(period)
                .toCommand();
        return command;
    }

    private ScheduleCyclePolicy givenReservationAt(LocalDateTime givenAt) {
        String reservationAt = givenAt.format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));
        ReservationPolicyValue policyValue = ReservationPolicyValue.of(reservationAt);
        return ScheduleCyclePolicy.of(policyValue);
    }
}
