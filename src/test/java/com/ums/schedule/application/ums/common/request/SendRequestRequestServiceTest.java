package com.ums.schedule.application.ums.common.request;

import com.ums.schedule.application.ums.common.send.JobManager;
import com.ums.schedule.application.ums.common.send.email.EmailJobManager;
import com.ums.schedule.common.code.common.CommonCode;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.request.SendRequestStatus;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestRepository;
import com.ums.schedule.domain.request.state.SendRequestReadyState;
import com.ums.schedule.fixture.entity.ScheduleEntityBuilder;
import com.ums.schedule.fixture.entity.SendRequestEntityBuilder;
import com.ums.schedule.fixture.entity.TargetUploadReportEntityBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SendRequestRequestServiceTest {
    @Spy private List<JobManager> jobManagers = new ArrayList<>();
    @Spy private EnumMapperFactory factory;

    @Mock private SendRequestRepository repository;
    @Mock private EmailJobManager emailJobManager;

    @InjectMocks private SendRequestRequestService requestService;

    private ScheduleEntityBuilder scheduleEntityBuilder;
    private SendRequestEntityBuilder sendRequestEntityBuilder;

    @BeforeEach
    void setUp() {
        factory.register(CommonCode.class);
        jobManagers.add(emailJobManager);
        sendRequestEntityBuilder = SendRequestEntityBuilder.builder()
                .schedule(ScheduleEntityBuilder.builder().build())
                .currentTargetUpload(TargetUploadReportEntityBuilder.builder().build())
                .state(new SendRequestReadyState())
        ;
    }

    @Test
    @DisplayName("send_request를 조회한다.")
    void shouldFindSendRequest() {
        doReturn(Optional.of(sendRequestEntityBuilder.build()))
                .when(repository).findById(anyLong());
        doReturn(true).when(emailJobManager).supports(any());

        requestService.request(1L, null);

        verify(repository).findById(anyLong());
    }

    @Nested
    @DisplayName("예외 발생 시")
    class WhenThrowException {
        @Test
        @DisplayName("스케쥴이 존재하지 않으면 예외가 발생한다.")
        void whenScheduleDoesNotExist() {

        }

        @Test
        @DisplayName("스케쥴이 이용가능한 상태가 아니면 예외가 발생한다.")
        void whenScheduleUnAvailable() {

        }

        @Test
        @DisplayName("대상자 업로드가 존재하지 않으면 예외가 발생한다.")
        void whenTargetUploadReportDoesNotExist() {

        }

        @Test
        @DisplayName("대상자 업로드 상태가 COMPLETED가 아니면 예외가 발생한다.")
        void whenTargetUploadReportStateIsNotComplete() {

        }
    }

    @Nested
    @DisplayName("스케쥴 타입이 실시간일 때")
    class WhenScheduleTypeIsRealtime {

        @Test
        @DisplayName("send_request의 상태는 SENDING으로 변경된다.")
        void shouldChangeStateToSending() {
            SendRequest sendRequest = sendRequestEntityBuilder.build();
            doReturn(Optional.of(sendRequest))
                    .when(repository).findById(anyLong());
            doReturn(true).when(emailJobManager).supports(any());

            requestService.request(1L,null);

            assertThat(sendRequest.getState().getCurrentCode()).isEqualTo(SendRequestStatus.SENDING);
        }


        @Test
        @DisplayName("channelType이 이메일이면 EmailSendJobManager를 호출한다.")
        void shouldInvokeEmailJobManager_whenChannelTypeIsEmail() {
            SendRequest sendRequest = sendRequestEntityBuilder.build();
            doReturn(Optional.of(sendRequest))
                    .when(repository).findById(anyLong());
            doReturn(true).when(emailJobManager).supports(any());

            requestService.request(1L,null);

            verify(emailJobManager).manage(any());
        }

        @Test
        @DisplayName("JobManager가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenJobManagerCouldNotFind() {

        }
    }

    @Nested
    @DisplayName("스케쥴 타입이 실시간이 아닐 때")
    class WhenScheduleTypeIsNotRealtime {
        @Test
        @DisplayName("send_request의 상태는 REQUEST으로 변경된다.")
        void shouldChangeStateToSending() {

        }

        @Test
        @DisplayName("EJobManager를 호출하지 않는다.")
        void shouldInvokeEmailJobManager() {

        }
    }
}