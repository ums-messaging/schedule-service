package com.ums.schedule.application.sendrequest;

import com.ums.schedule.application.exception.schedule.ScheduleNotFoundException;
import com.ums.schedule.application.sendrequest.command.SendRequestCreateCommand;
import com.ums.schedule.application.ums.common.request.SendRequestCreateService;
import com.ums.schedule.application.ums.common.request.model.SendRequestCreateResult;
import com.ums.schedule.application.sendrequest.target.result.TargetUploadResult;
import com.ums.schedule.common.config.SendRequestProperties;
import com.ums.schedule.domain.exception.validation.DuplicateViolationException;
import com.ums.schedule.domain.exception.validation.ValidationException;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestRepository;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleJpaRepository;

import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.fixture.sendrequest.SendRequestCreateCommandBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendRequestCreateServiceTest {
    @Mock private ScheduleJpaRepository scheduleRepository;
    @Mock private SendRequestRepository sendRequestRepository;
    @Mock private TargetUploadCreateService targetUploadService;
    @Mock private SendRequestProperties properties;
    @InjectMocks private SendRequestCreateService sendRequestService;

    private SendRequestCreateCommandBuilder commandBuilder;

    private Schedule schedule;
    private SendMessage sendMessage;
    private TargetUploadResult uploadResult;

    @BeforeEach
    void setUp() {
        this.sendMessage = mock(SendMessage.class);
        this.schedule = mock(Schedule.class);
        this.uploadResult = mock(TargetUploadResult.class);
        this.commandBuilder = SendRequestCreateCommandBuilder.builder();
    }

    @Nested
    @DisplayName("고객 키 테스트")
    class WhenCustomerKey {
        @Test
        @DisplayName("고객 키가 존재하면 발송 요청 데이터는 저장되지 않는다.")
        void shouldThrowException_whenCustomerKeyExists() {
            SendRequestCreateCommand command = commandBuilder.build();

            doReturn(Optional.ofNullable(schedule)).when(scheduleRepository).findById(any());
            doReturn(true).when(sendRequestRepository).existsByCustomerRequestKey(any());

            ValidationException expect = DuplicateViolationException.fieldOf("customer_key");

            assertThatThrownBy(() -> sendRequestService.create(command, sendMessage))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
            verify(sendRequestRepository, never()).save(any(SendRequest.class));
        }

        @Test
        @DisplayName("고객 요청 키를 조회한다.")
        void shouldGetCustomerRequestKey() {
            SendRequestCreateCommand command = commandBuilder.build();

            doReturn(Optional.ofNullable(schedule)).when(scheduleRepository).findById(any());
            doReturn(mock(TargetUploadResult.class)).when(targetUploadService).create(any(), any());

            sendRequestService.create(command, sendMessage);

            verify(sendRequestRepository).existsByCustomerRequestKey(any());
        }

    }

    @Nested
    @DisplayName("스케쥴 테스트")
    class WhenSchedule {
        @Test
        @DisplayName("스케쥴을 조회한다.")
        void shouldGetSchedule() {
            SendRequestCreateCommand command = commandBuilder.build();

            doReturn(Optional.ofNullable(schedule)).when(scheduleRepository).findById(any());
            doReturn(false).when(sendRequestRepository).existsByCustomerRequestKey(any());
            doReturn(mock(TargetUploadResult.class)).when(targetUploadService).create(any(), any());

            sendRequestService.create(command, sendMessage);

            verify(scheduleRepository).findById(command.scheduleId());
        }

        @Test
        @DisplayName("스케쥴이 존재하지 않을 시, 예외가 발생한다.")
        void shouldThrowException_whenScheduleDoesNotExist() {
            SendRequestCreateCommand command = commandBuilder.build();

            doReturn(Optional.ofNullable(null)).when(scheduleRepository).findById(any());

            ScheduleNotFoundException expect = ScheduleNotFoundException.of(command.scheduleId());

            assertThatThrownBy(() -> sendRequestService.create(command, sendMessage))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
    }

    @Nested
    @DisplayName("재시도 횟수")
    class WhenRetryCount {
        @Test
        @DisplayName("재시도 횟수 입력 값이 없으면 설정 파일에서 가져온 값이 반환된다.")
        void shouldReturnConfigureValue_whenRetryCountCommandDoesNotExist() {
            SendRequestCreateCommand command = commandBuilder.retryCnt(null).build();

            doReturn(Optional.ofNullable(schedule)).when(scheduleRepository).findById(any());
            doReturn(false).when(sendRequestRepository).existsByCustomerRequestKey(any());
            doReturn(uploadResult).when(targetUploadService).create(any(), any());
            doReturn(4).when(properties).retryCount();

            SendRequestCreateResult result = sendRequestService.create(command, sendMessage);
            SendRequest sendRequest = result.sendRequest();

            assertThat(sendRequest.getRetryCnt()).isEqualTo(4);
            verify(properties).retryCount();
        }

        @Test
        @DisplayName("재시도 횟수 입력 값이 존재하면, 설정 파일에서 재시도 횟수는 조회되지 않는다.")
        void shouldNotGetConfigureValue_whenRetryCountCommandExists() {
            SendRequestCreateCommand command = commandBuilder.retryCnt(3).build();

            doReturn(Optional.ofNullable(schedule)).when(scheduleRepository).findById(any());
            doReturn(false).when(sendRequestRepository).existsByCustomerRequestKey(any());
            doReturn(uploadResult).when(targetUploadService).create(any(), any());

            SendRequestCreateResult result = sendRequestService.create(command, sendMessage);
            SendRequest sendRequest = result.sendRequest();

            assertThat(sendRequest.getRetryCnt()).isEqualTo(3);
            verify(properties, never()).retryCount();
        }
    }

    @Nested
    @DisplayName("대상자 업로드")
    class WhenTargetUploadReport {
        @Test
        @DisplayName("대상자 업로드가 생성된다.")
        void shouldCreateTargetUploadReport() {
            SendRequestCreateCommand command = commandBuilder.build();

            doReturn(Optional.ofNullable(schedule)).when(scheduleRepository).findById(any());
            doReturn(false).when(sendRequestRepository).existsByCustomerRequestKey(any());
            doReturn(mock(TargetUploadResult.class)).when(targetUploadService).create(any(), any());

            sendRequestService.create(command, sendMessage);

            verify(targetUploadService).create(any(), any());
        }

        @Test
        @DisplayName("발송 요청 데이터 저장 실패 시 대상자 업로드는 생성되지 않는다.")
        void shouldNotCreateTargetUploadReport_whenSendRequestSaveFails() {
            SendRequestCreateCommand command = commandBuilder.build();

            doReturn(Optional.ofNullable(mock(Schedule.class))).when(scheduleRepository).findById(any());
            doReturn(false).when(sendRequestRepository).existsByCustomerRequestKey(any());
            doThrow(new DataIntegrityViolationException("not null")).when(sendRequestRepository).save(any(SendRequest.class));


            assertThatThrownBy(() -> sendRequestService.create(command, sendMessage));
            verify(targetUploadService).create(any(), any());
        }
    }


    @Nested
    @DisplayName("발송 요청 데이터 저장")
    class WhenSendRequestSave {
        @Test
        @DisplayName("발송 요청 데이터가 정상 저장된다.")
        void shouldSaveSendRequest() {
            SendRequestCreateCommand command = commandBuilder.build();

            doReturn(Optional.ofNullable(mock(Schedule.class))).when(scheduleRepository).findById(any());
            doReturn(false).when(sendRequestRepository).existsByCustomerRequestKey(any());
            doReturn(mock(TargetUploadResult.class)).when(targetUploadService).create(any(), any());

            sendRequestService.create(command, sendMessage);

            verify(sendRequestRepository).save(any());
        }


        @Test
        @DisplayName("발송 요청 데이터 생성 실패 시, 발송 요청 데이터는 저장되지 않는다.")
        void shouldNotSaveSendRequest_whenSendRequestCreateFails() {
            SendRequestCreateCommand command = commandBuilder.channelType(null).build();

            doReturn(Optional.ofNullable(mock(Schedule.class))).when(scheduleRepository).findById(any());
            doReturn(false).when(sendRequestRepository).existsByCustomerRequestKey(any());

            assertThatThrownBy(() -> sendRequestService.create(command, sendMessage));
            verify(sendRequestRepository, never()).save(any(SendRequest.class));
        }
    }
}