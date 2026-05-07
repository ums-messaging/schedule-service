package com.ums.schedule.domain.request;

import com.ums.schedule.code.schedule.CycleCdEnum;
import com.ums.schedule.code.send.ChannelTypeEnum;
import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.domain.request.exception.InvalidScheduleException;
import com.ums.schedule.domain.request.state.SendRequestCreateState;
import com.ums.schedule.domain.request.state.SendRequestState;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.domain.schedule.cycle_policy.CyclePolicyValue;
import com.ums.schedule.domain.schedule.cycle_policy.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule.period.SchedulePeriod;
import com.ums.schedule.domain.schedule.period.SchedulePeriodTestBuilder;
import com.ums.schedule.fixture.ScheduleDomainFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SendRequestTest {

    @Test
    @DisplayName("SendRequest 생성 시 status는 SendRequestCreateState가 반환된다.")
    void shouldReturnSendRequestStateIsSendRequestCreateState_whenSendRequestCreate() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
        SendRequestState expect = sendRequest.getState();

        assertThat(expect).isInstanceOf(SendRequestCreateState.class);
        assertThat(expect.currentSendRequestStatus()).isEqualTo(SendRequestStatusEnum.CREATE);
    }

    @Test
    @DisplayName("스케쥴 추가 시 스케쥴 상태가 RUNNING 이 아니면 익셉션이 발생한다. ")
    void shouldThrowException_whenScheduleStatusIsNotRunning() {
        Schedule schedule = ScheduleDomainFixture.createSchedule();
        CustomerRequestKey key = CustomerRequestKey.of(UUID.randomUUID().toString(), UUID.randomUUID().toString(), false);

        InvalidScheduleException expect = InvalidScheduleException.invalidSchedule();

        assertThatThrownBy(() -> SendRequest.of(schedule, key, ChannelTypeEnum.EMAIL))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("스케쥴 추가 시 현재 날짜가 스케쥴 기간에 포함되지 않으면 익셉션이 발생한다. ")
    void shouldThrowException_whenTodayDoesNotContainInSchedulePeriod() {
        LocalDateTime startDtm = LocalDateTime.now().plusDays(1);
        LocalDateTime endDtm = startDtm.plusMonths(1);
        SchedulePeriod schedulePeriod = SchedulePeriodTestBuilder.builder().build();

        ScheduleCyclePolicy cyclePolicy = ScheduleCyclePolicy.of(CyclePolicyValue.of(CycleCdEnum.ALWAYS, 0));
        CustomerRequestKey key = CustomerRequestKey.of(UUID.randomUUID().toString(), UUID.randomUUID().toString(), false);

        Schedule given = ScheduleTestBuilder.builder().build();

        InvalidScheduleException expect = InvalidScheduleException.invalidSchedule();

        assertThatThrownBy(() -> SendRequest.of(given, key, ChannelTypeEnum.EMAIL))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }
}