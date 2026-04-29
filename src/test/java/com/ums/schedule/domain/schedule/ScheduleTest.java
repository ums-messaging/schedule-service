package com.ums.schedule.domain.schedule;

import com.ums.schedule.code.schedule.ScheduleStatusEnum;
import com.ums.schedule.domain.channel.email.EmailSendRequest;
import com.ums.schedule.domain.channel.email.message.EmailBody;
import com.ums.schedule.domain.schedule.cycle_policy.ReservationPolicyValue;
import com.ums.schedule.domain.schedule.cycle_policy.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule.exception.InvalidCycleValueException;
import com.ums.schedule.domain.schedule.exception.InvalidSchedulePeriodException;
import com.ums.schedule.domain.schedule.exception.InvalidScheduleStatusException;
import com.ums.schedule.domain.schedule.period.SchedulePeriod;
import com.ums.schedule.domain.schedule.status.ScheduleActiveStatus;
import com.ums.schedule.domain.schedule.status.ScheduleRunningStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.ums.schedule.code.schedule.CycleCdEnum.ALWAYS;
import static com.ums.schedule.code.schedule.ScheduleTypeEnum.REALTIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScheduleTest {
    @Test
    @DisplayName("스케쥴 처음 생성 시 스케쥴 상태는 ACTIVE이어야 한다.")
    void shouldReturnScheduleStatusIsActive_whenScheduleCreate() {
        SchedulePeriod period = SchedulePeriod.of(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusMonths(1));
        LocalDateTime now = LocalDateTime.now().plusDays(3).withSecond(0).withNano(0);
        String reservationDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));

        ReservationPolicyValue policyValue = ReservationPolicyValue.of(reservationDate);
        ScheduleCyclePolicy policy = ScheduleCyclePolicy.of(policyValue);
        Schedule result = Schedule.of("스케쥴 명", period, policy);

        assertThat(result.getStatus()).isEqualTo(ScheduleStatusEnum.ACTIVE);
        assertThat(result.getScheduleStatus()).isInstanceOf(ScheduleActiveStatus.class);
    }

    @Nested
    @DisplayName("스케쥴 상태")
    class ScheduleStatusTest {
        @Test
        @DisplayName("스케쥴 상태를 RUNNING으로 변경하면 status는 RUNNING으로 변경된다.")
        void shouldChangeToRunning_whenScheduleStatusChangeRunning() {
            LocalDateTime scheduleStartAt = LocalDateTime.now().plusDays(1);
            LocalDateTime scheduleEndAt = scheduleStartAt.plusDays(10);
            SchedulePeriod period = SchedulePeriod.of(scheduleStartAt, scheduleEndAt);

            ScheduleCyclePolicy policy = new ScheduleCyclePolicy(REALTIME, ALWAYS, null);

            Schedule schedule = Schedule.of("스케쥴", period, policy);
            schedule.toRunning();

            assertThat(schedule.getScheduleStatus()).isInstanceOf(ScheduleRunningStatus.class);
            assertThat(schedule.getStatus()).isEqualTo(ScheduleStatusEnum.RUNNING);
        }
    }

    @Nested
    @DisplayName("예약 주기 스케쥴일 경우")
    class ScheduleTypeReservationTest {
        @Test
        @DisplayName("예약 날짜가 스케쥴 시작 날짜보다 이전이면, 오류가 발생한다.")
        void shouldThrowException_whenReservationDateBeforeAtScheduleStartAt() {
            LocalDateTime scheduleStartAt = LocalDateTime.now().plusDays(1);
            LocalDateTime scheduleEndAt = scheduleStartAt.plusDays(10);
            SchedulePeriod period = SchedulePeriod.of(scheduleStartAt, scheduleEndAt);

            LocalDateTime reservationAt = scheduleStartAt.minusMinutes(1);
            String given = reservationAt.format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));
            ReservationPolicyValue policyValue = ReservationPolicyValue.of(given);
            ScheduleCyclePolicy policy = ScheduleCyclePolicy.of(policyValue);

            assertThatThrownBy(() -> Schedule.of("스케쥴 명", period, policy))
                    .isInstanceOf(InvalidCycleValueException.class)
                    .hasMessage(InvalidCycleValueException.compareToReservationDate().getMessage());

        }

        @Test
        @DisplayName("예약 날짜가 스케쥴 종료 날짜보다 이후이면, 오류가 발생한다.")
        void shouldThrowException_whenReservationDateAfterAtScheduleEndAt() {
            LocalDateTime scheduleStartAt = LocalDateTime.now().plusDays(1);
            LocalDateTime scheduleEndAt = scheduleStartAt.plusDays(10);
            SchedulePeriod period = SchedulePeriod.of(scheduleStartAt, scheduleEndAt);

            LocalDateTime reservationAt = scheduleEndAt.plusMinutes(1);
            String given = reservationAt.format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));
            ReservationPolicyValue policyValue = ReservationPolicyValue.of(given);
            ScheduleCyclePolicy policy = ScheduleCyclePolicy.of(policyValue);

            assertThatThrownBy(() -> Schedule.of("스케쥴 명", period, policy))
                    .isInstanceOf(InvalidCycleValueException.class)
                    .hasMessage(InvalidCycleValueException.compareToReservationDate().getMessage());
        }

        @Test
        @DisplayName("예약 날짜가 스케쥴 기간 안에 포함되면, ScheduleCyclePolicy와 SchedulePeriod는 NULL이 아니다.")
        void shouldReturnSchedulePeriodAndCyclePolicyIsNotNull_whenReservationAtContainsSchedulePerios() {
            LocalDateTime scheduleStartAt = LocalDateTime.now().plusDays(1);
            LocalDateTime scheduleEndAt = scheduleStartAt.plusDays(10);
            SchedulePeriod period = SchedulePeriod.of(scheduleStartAt, scheduleEndAt);

            LocalDateTime reservationAt = scheduleEndAt.minusDays(1);
            String given = reservationAt.format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));
            ReservationPolicyValue policyValue = ReservationPolicyValue.of(given);
            ScheduleCyclePolicy policy = ScheduleCyclePolicy.of(policyValue);

            Schedule result = Schedule.of("스케쥴 명", period, policy);

            assertThat(result.getCyclePolicy()).isNotNull();
            assertThat(result.getSchedulePeriod()).isNotNull();
        }
    }

    @Test
    @DisplayName("SendRequest를 추가할 때, 스케쥴 상태가 유효하지 않으면 익셉션이 발생한다.")
    void shouldThrowException_whenScheduleStatusInvalid() {
        SchedulePeriod period = SchedulePeriod.of(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusMonths(1));
        LocalDateTime now = LocalDateTime.now().plusDays(3).withSecond(0).withNano(0);
        String reservationDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));

        ReservationPolicyValue policyValue = ReservationPolicyValue.of(reservationDate);
        ScheduleCyclePolicy policy = ScheduleCyclePolicy.of(policyValue);
        Schedule result = Schedule.of("스케쥴 명", period, policy);
        result.toInActive();

//        assertThatThrownBy(() -> result.addSendRequestList(EmailSendRequest.of(EmailBody.of())))
//                .isInstanceOf(InvalidScheduleStatusException.class);
    }

    @Test
    @DisplayName("SendRequest를 추가할 때, 스케쥴 기간이 유효하지 않으면 익셉션이 발생한다.")
    void shouldThrowException_whenSchedulePeriodIsInvalid() {
        SchedulePeriod period = SchedulePeriod.of(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusMonths(1));
        LocalDateTime now = LocalDateTime.now().plusDays(3).withSecond(0).withNano(0);
        String reservationDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));

        ReservationPolicyValue policyValue = ReservationPolicyValue.of(reservationDate);
        ScheduleCyclePolicy policy = ScheduleCyclePolicy.of(policyValue);
        Schedule result = Schedule.of("스케쥴 명", period, policy);

//        assertThatThrownBy(() -> result.addSendRequestList(EmailSendRequest.of(EmailBody.of())))
//                .isInstanceOf(InvalidSchedulePeriodException.class);

    }
}