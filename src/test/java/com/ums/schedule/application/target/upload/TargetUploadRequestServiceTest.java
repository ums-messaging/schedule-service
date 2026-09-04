package com.ums.schedule.application.target.upload;

import com.ums.schedule.application.target.TargetUploadRequestService;
import com.ums.schedule.application.target.report.model.TargetUploadRequestResult;
import com.ums.schedule.common.code.request.SendRequestStatus;
import com.ums.schedule.common.code.target_upload.TargetUploadStatus;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.request.state.SendRequestHoldingState;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.target.upload.exception.InvalidTargetUploadStateException;
import com.ums.schedule.domain.target.upload.state.TargetUploadCompleteState;
import com.ums.schedule.domain.target.upload.state.TargetUploadCreateState;
import com.ums.schedule.domain.target.upload.state.TargetUploadParsingState;
import com.ums.schedule.domain.target.upload.state.TargetUploadWaitingState;
import com.ums.schedule.fixture.entity.SendRequestEntityBuilder;
import com.ums.schedule.fixture.entity.TargetUploadReportEntityBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TargetUploadRequestServiceTest {
    @InjectMocks private TargetUploadRequestService requestService;

    private TargetUploadReportEntityBuilder reportBuilder;
    private SendRequestEntityBuilder requestBuilder;

    @BeforeEach
    void setUp() {
        requestBuilder = SendRequestEntityBuilder.builder()
                .state(new SendRequestHoldingState())
                .sendMessage(mock(SendMessage.class))

        ;
        reportBuilder = TargetUploadReportEntityBuilder.builder()
                .uploadStatus(new TargetUploadWaitingState())
                .sendRequest(requestBuilder.build());
    }

    @Test
    @DisplayName("대상자 업로드 리포트 상태가 REQUEST로 변경된다.")
    void shouldChangeStateToChange() {
        TargetUploadRequestResult result = requestService.request(reportBuilder.build());

        assertThat(result.targetUploadStatus()).isEqualTo(TargetUploadStatus.REQUEST);

    }

    @Nested
    @DisplayName("발송 요청 상태 변경")
    class WhenChangeSendRequestState {
        @Test
        @DisplayName("대상자 업로드 상태가 CREATE이면 예외가 발생한다.")
        void shouldThrowException_whenStateIsCreate() {
            TargetUploadReport report = reportBuilder.uploadStatus(new TargetUploadCreateState()).build();

            assertThatThrownBy(() -> requestService.request(report))
                    .isInstanceOf(InvalidTargetUploadStateException.class);
        }

        @Test
        @DisplayName("대상자 업로드 상태가 WAITING이면 HOLDING으로 변경된다.")
        void shouldChangeStateToHolding_whenStateIsWaiting() {
            TargetUploadReport report = reportBuilder.uploadStatus(new TargetUploadWaitingState()).build();

            TargetUploadRequestResult result = requestService.request(report);

            assertThat(result.sendRequestStatus()).isEqualTo(SendRequestStatus.HOLDING);

        }

        @Test
        @DisplayName("대상자 업로드 상태가 PARSING이면 예외가 발생한다.")
        void shouldThrowException_whenStateIsParsing() {
            TargetUploadReport report = reportBuilder.uploadStatus(new TargetUploadParsingState()).build();

            assertThatThrownBy(() -> requestService.request(report))
                    .isInstanceOf(InvalidTargetUploadStateException.class);
        }

        @Test
        @DisplayName("대상자 업로드 상태가 COMPLETED이면 예외가 발생한다.")
        void shouldThrowException_whenStateIsCompleted() {
            TargetUploadReport report = reportBuilder.uploadStatus(new TargetUploadCompleteState()).build();

            assertThatThrownBy(() -> requestService.request(report))
                    .isInstanceOf(InvalidTargetUploadStateException.class);
        }
    }
}