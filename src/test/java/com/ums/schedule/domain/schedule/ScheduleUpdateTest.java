package com.ums.schedule.domain.schedule;

import com.ums.schedule.application.schedule.dto.ScheduleUpdateCommand;
import com.ums.schedule.domain.schedule.exception.InvalidSchedulePeriodException;
import com.ums.schedule.domain.schedule.exception.InvalidScheduleStatusException;
import com.ums.schedule.domain.schedule.exception.ScheduleNameRequiredException;
import com.ums.schedule.domain.schedule.period.SchedulePeriod;
import com.ums.schedule.domain.schedule.period.SchedulePeriodTestBuilder;
import com.ums.schedule.domain.state.schedule.ScheduleActiveStatus;
import com.ums.schedule.domain.state.schedule.ScheduleInActiveStatus;
import com.ums.schedule.domain.state.schedule.ScheduleRunningStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ScheduleUpdateTest {
    @Test
    @DisplayName("스케쥴 상태가 ACTIVE가 아니면 익셉션이 발생한다.")
    void shouldThrowException_whenScheduleStatusIsNotActive() {
        Schedule schedule = ScheduleTestBuilder.builder().status(new ScheduleRunningStatus()).build();
        ScheduleUpdateCommand command = ScheduleTestBuilder.builder().scheduleName("schedule").toUpdateCommand();

        InvalidScheduleStatusException expect = InvalidScheduleStatusException.invalidStatus();

        assertThatThrownBy(() -> schedule.update(command))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("현재 날짜가 스케쥴 종료 날짜 이후이면 익셉션이 발생한다.")
    void shouldThrowException_whenTodayIsAfterScheduleEndAt() {
        SchedulePeriod period = SchedulePeriodTestBuilder.builder()
                .scheduleStartAt(LocalDate.now().minusMonths(1))
                .scheduleEndAt(LocalDate.now().minusWeeks(1))
                .build();

        Schedule schedule = ScheduleTestBuilder.builder()
                .status(new ScheduleActiveStatus())
                .schedulePeriod(period)
                .build();

        ScheduleUpdateCommand command = ScheduleTestBuilder.builder().scheduleName("schedule").toUpdateCommand();

        InvalidSchedulePeriodException expect = InvalidSchedulePeriodException.expired();

        assertThatThrownBy(() -> schedule.update(command))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("현재 날짜가 스케쥴 종료 날짜 이후이고, 상태가 INACTIVE가 아니면 상태는 INACTIVE로 변경된다.")
    void shouldThrowException_whenTodayIsAfterScheduleEndAtAndStatusIsNotInActive() {
        SchedulePeriod period = SchedulePeriodTestBuilder.builder()
                .scheduleStartAt(LocalDate.now().minusMonths(1))
                .scheduleEndAt(LocalDate.now().minusWeeks(1))
                .build();

        Schedule schedule = ScheduleTestBuilder.builder()
                .status(new ScheduleActiveStatus())
                .schedulePeriod(period)
                .build();

        ScheduleUpdateCommand command = ScheduleTestBuilder.builder().scheduleName("schedule").toUpdateCommand();

        assertThatThrownBy(() -> schedule.update(command));
        assertThat(schedule.getScheduleStatus()).isInstanceOf(ScheduleInActiveStatus.class);
    }

    @Test
    @DisplayName("스케쥴 명이 공백일 경우 익셉션이 발생한다.")
    void shouldThrowException_whenScheduleNameIsEmpty() {
        Schedule schedule = ScheduleTestBuilder.builder()
                .status(new ScheduleActiveStatus())
                .build();

        ScheduleUpdateCommand command = ScheduleTestBuilder.builder().scheduleName(" ").toUpdateCommand();

        ScheduleNameRequiredException expect = ScheduleNameRequiredException.of();

        assertThatThrownBy(() -> schedule.update(command))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("스케쥴 명 입력 시 입력한 스케쥴 명으로 변경된다.")
    void shouldChangeScheduleName_whenScheduleNameUpdateCommand() {
        String beforeName = "asis";
        String changeToName = "tobe";

        Schedule schedule = ScheduleTestBuilder.builder()
                .status(new ScheduleActiveStatus())
                .scheduleName(beforeName)
                .build();

        ScheduleUpdateCommand command = ScheduleTestBuilder.builder().scheduleName(changeToName).toUpdateCommand();
        schedule.update(command);

        assertThat(schedule.getName()).isEqualTo(changeToName);
    }
}
