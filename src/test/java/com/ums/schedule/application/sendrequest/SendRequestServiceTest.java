package com.ums.schedule.application.sendrequest;

import com.ums.schedule.adapter.api.request.request.SendRequestCreateRequest;
import com.ums.schedule.adapter.api.sendrequest.email.request.SendRequestCreateRequestBuilder;
import com.ums.schedule.application.sendrequest.target.report.TargetUploadReportFactory;
import com.ums.schedule.common.exception.PolicyViolationException;
import com.ums.schedule.common.exception.validation.DuplicateViolationException;
import com.ums.schedule.common.exception.validation.RequiredException;
import com.ums.schedule.common.exception.validation.ValidationException;
import com.ums.schedule.domain.sendrequest.SendRequestRepository;
import com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleJpaRepository;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.domain.schedule.exception.ScheduleNotExecutableException;
import com.ums.schedule.domain.schedule.exception.ScheduleNotFoundException;
import com.ums.schedule.domain.schedule.policy.SchedulePeriod;
import com.ums.schedule.domain.schedule.policy.SchedulePeriodTestBuilder;
import com.ums.schedule.domain.schedule.state.ScheduleInActiveStatus;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendRequestServiceTest {
    @Mock private ScheduleJpaRepository scheduleRepository;
    @Mock private SendRequestRepository sendRequestRepository;
    @Mock private TargetUploadReportFactory targetUploadService;
    @InjectMocks private SendRequestService sendRequestService;

    @Test
    @DisplayName("고객 키가 존재하면 익셉션이 발생한다.")
    void shouldThrowException_whenCustomerKeyExists() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        SendRequestCreateRequest request = SendRequestCreateRequestBuilder.builder().build();

        doReturn(Optional.ofNullable(schedule)).when(scheduleRepository).findById(any());
        doReturn(true).when(sendRequestRepository).existsByCustomerRequestKey(any());

        ValidationException expect = DuplicateViolationException.fieldOf("customer key");

        assertThatThrownBy(() -> sendRequestService.create(UUID.randomUUID().toString(), ChannelTypeEnum.EMAIL, request))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("채널 타입이 NULL이면 익셉션이 발생한다.")
    void shouldThrowException_whenChannTypeIsNull() {
        Schedule givenSchedule = ScheduleTestBuilder.builder().build();
        SendRequestCreateRequest request = SendRequestCreateRequestBuilder.builder().build();

        doReturn(Optional.ofNullable(givenSchedule)).when(scheduleRepository).findById(any());
        doReturn(false).when(sendRequestRepository).existsByCustomerRequestKey(any());

        ValidationException expect = RequiredException.fieldOf("channel_type");

        assertThatThrownBy(() -> sendRequestService.create(UUID.randomUUID().toString(), null, request))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Nested
    @DisplayName("스케쥴 테스트")
    class WhenSchedule {
        @Test
        @DisplayName("스케쥴이 NULL이면 익셉션이 발생한다.")
        void shouldThrowException_whenScheduleIsNull() {
            SendRequestCreateRequest request = SendRequestCreateRequestBuilder.builder().build();

            doReturn(Optional.ofNullable(null)).when(scheduleRepository).findById(any());
            doReturn(false).when(sendRequestRepository).existsByCustomerRequestKey(any());

            ScheduleNotFoundException expect = ScheduleNotFoundException.of();

            assertThatThrownBy(() -> sendRequestService.create(UUID.randomUUID().toString(), ChannelTypeEnum.EMAIL, request))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
    }


    @Test
    @DisplayName("스케쥴 상태가 비활성화 상태이면 예외가 발생한다.")
    void shouldThrowException_whenScheduleStateIsInActive() {
        Schedule givenSchedule = ScheduleTestBuilder.builder().status(new ScheduleInActiveStatus()).build();
        SendRequestCreateRequest request = SendRequestCreateRequestBuilder.builder().build();

        doReturn(Optional.ofNullable(givenSchedule)).when(scheduleRepository).findById(any());
        doReturn(false).when(sendRequestRepository).existsByCustomerRequestKey(any());

        PolicyViolationException expect = ScheduleNotExecutableException.inActiveOf();

        assertThatThrownBy(() -> sendRequestService.create(UUID.randomUUID().toString(), ChannelTypeEnum.EMAIL, request))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("이미 만료된 스케쥴이면 익셉션이 발생한다.")
    void shouldThrowException_whenCurrentDateDoesNotContainInSchedulePeriod() {
        SchedulePeriod givenSchedulePeriod = SchedulePeriodTestBuilder.builder()
                .scheduleStartAt(LocalDate.now().minusMonths(1))
                .scheduleEndAt(LocalDate.now().minusWeeks(1)).build();
        Schedule givenSchedule = ScheduleTestBuilder.builder().schedulePeriod(givenSchedulePeriod).build();
        SendRequestCreateRequest request = SendRequestCreateRequestBuilder.builder().build();

        doReturn(Optional.ofNullable(givenSchedule)).when(scheduleRepository).findById(any());
        doReturn(false).when(sendRequestRepository).existsByCustomerRequestKey(any());

        ScheduleNotExecutableException expect = ScheduleNotExecutableException.expiredOf();

        assertThatThrownBy(() -> sendRequestService.create(UUID.randomUUID().toString(), ChannelTypeEnum.EMAIL, request))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("TargetUploadReport가 생성된다.")
    void shouldCreateTargetUploadReport() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        SendRequestCreateRequest request = SendRequestCreateRequestBuilder.builder().build();
        TargetUploadReport targetUpload = TargetUploadTestBuilder.builder().build();

        doReturn(Optional.ofNullable(schedule)).when(scheduleRepository).findById(any());
        doReturn(false).when(sendRequestRepository).existsByCustomerRequestKey(any());
        doReturn(targetUpload).when(targetUploadService).create(any(), any(), any());

        sendRequestService.create(UUID.randomUUID().toString(), ChannelTypeEnum.EMAIL, request);

        verify(targetUploadService).create(any(),any(), any());
    }

    @Test
    @DisplayName("sendRequest가 저장된다.")
    void shouldSaveSendRequest() {

    }
}