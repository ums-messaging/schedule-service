package com.ums.schedule.domain.request.target.upload;

import com.ums.schedule.application.target.report.model.TargetUploadReportCreateContext;
import com.ums.schedule.common.code.api.SendRequestErrorCode;
import com.ums.schedule.common.code.api.TargetUploadErrorCode;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.target_upload.TargetUploadType;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.exception.InvalidSendRequestStateException;
import com.ums.schedule.domain.request.state.*;
import com.ums.schedule.common.code.target_upload.TargetUploadFormatEnum;
import com.ums.schedule.common.code.target_upload.TargetUploadStatus;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.target.upload.exception.InvalidTargetUploadStateException;
import com.ums.schedule.domain.target.upload.exception.TargetUploadPolicyViolationException;
import com.ums.schedule.domain.target.upload.state.*;
import com.ums.schedule.fixture.entity.SendRequestEntityBuilder;
import com.ums.schedule.fixture.target_upload.TargetUploadReportCreateContextBuilder;
import com.ums.schedule.fixture.entity.TargetUploadReportEntityBuilder;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class TargetUploadReportTest {
    private TargetUploadReportCreateContextBuilder contextBuilder;

    @BeforeEach
    void setUp() {
        this.contextBuilder = TargetUploadReportCreateContextBuilder.builder();
    }

    @Nested
    @DisplayName("대상자 업로드 리포트 생성")
    class WhenTargetUploadReport {
        @Test
        @DisplayName("ID가 생성된다.")
        void shouldGenerateTargetUploadReportId() {
            TargetUploadReportCreateContext context = contextBuilder.build();

            TargetUploadReport report = TargetUploadReport.of(context);

            assertThat(report.getId()).isNotNull();
        }
        @Test
        @DisplayName("업로드 생성 시각이 생성된다.")
        void shouldGenerateUploadCreatedAt() {
            TargetUploadReportCreateContext context = contextBuilder.build();

            TargetUploadReport report = TargetUploadReport.of(context);

            assertThat(report.getCreatedAt().toLocalDate()).isEqualTo(LocalDate.now());
        }

        @Test
        @DisplayName("업로드 유형이 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenUploadTypeDoesNotExist() {
            TargetUploadReportCreateContext context = contextBuilder.uploadType(null).build();

            NullPointerException expect = new NullPointerException();

            assertThatThrownBy(() -> TargetUploadReport.of(context))
                    .isInstanceOf(expect.getClass())
                    .hasMessage("upload_type");
        }

        @Nested
        @DisplayName("다운로드 키")
        class WhenDownloadKey {
            private final String downloadKeyPrefix = "/target/upload/result";
            private final SendRequest sendRequest = SendRequestEntityBuilder.builder()
                    .id(1L)
                    .customerRequestKey("hyejin_company", "jang314")
                    .build();

            @Test
            @DisplayName("다운로드 키가 생성된다.")
            void shouldGenerateDownloadKey() {
                TargetUploadReportCreateContext context = contextBuilder
                        .sendRequest(sendRequest)
                        .channelType(ChannelType.EMAIL)
                        .downloadKeyPrefix(downloadKeyPrefix)
                        .build();

                TargetUploadReport report = TargetUploadReport.of(context);

                assertThat(report.getDownloadKey()).isNotEmpty();
            }

            @Test
            @DisplayName("다운로드 키는 정해진 규칙에 의해 생성된다.")
            void shouldGenerateDownloadKeyAccordingToRule() {
                TargetUploadReportCreateContext context = contextBuilder
                        .sendRequest(sendRequest)
                        .channelType(ChannelType.EMAIL)
                        .downloadKeyPrefix(downloadKeyPrefix)
                        .build();

                TargetUploadReport report = TargetUploadReport.of(context);
                String expect = "%s/%s.xlsx".formatted(downloadKeyPrefix, report.getId().toString());

                assertThat(report.getDownloadKey()).isEqualTo(expect);
            }

        }

        @Nested
        @DisplayName("발송 요청 데이터")
        class WhenSendRequest {
            private SendRequestEntityBuilder sendRequestBuilder;

            @BeforeEach
            void setUp() {
                sendRequestBuilder = SendRequestEntityBuilder.builder();
            }

            @Test
            @DisplayName("발송 요청 데이터와 연관 관계가 맺어진다.")
            void shouldCreateRelationWithSendRequest() {
                SendRequest sendRequest = sendRequestBuilder.build();
                TargetUploadReportCreateContext context = contextBuilder
                        .sendRequest(sendRequest)
                        .build();

                TargetUploadReport report = TargetUploadReport.of(context);

                assertThat(report.getSendRequest()).isNotNull();
                assertThat(sendRequest.getCurrentTargetUpload()).isEqualTo(report);
            }

            @Test
            @DisplayName("발송 요청 데이터가 존재하지 않으면 예외가 발생한다.")
            void shouldThrowException_whenSendRequestDoesNotExist() {
                TargetUploadReportCreateContext context = contextBuilder.sendRequest(null).build();

                NullPointerException expect = new NullPointerException();

                assertThatThrownBy(() -> TargetUploadReport.of(context))
                        .isInstanceOf(expect.getClass())
                        .hasMessage("send_request");
            }

            @Test
            @DisplayName("발송 요청 상태가 REQUEST이면, 예외가 발생한다.")
            void shouldThrowException_whenSendRequestStateIsRequest() {
                SendRequest sendRequest = sendRequestBuilder.state(new SendRequestRequestState())
                        .build();

                TargetUploadReportCreateContext context = contextBuilder.sendRequest(sendRequest).build();
                InvalidSendRequestStateException expect = InvalidSendRequestStateException.of(SendRequestErrorCode.SEND_REQUEST_REQUESTED);

                assertThatThrownBy(() -> TargetUploadReport.of(context))
                        .isInstanceOf(expect.getClass())
                        .hasMessage(expect.getMessage());
            }

            @Test
            @DisplayName("발송 요청 상태가 SENDING이면, 예외가 발생한다.")
            void shouldThrowException_whenSendRequestStateIsSending() {
                SendRequest sendRequest = sendRequestBuilder.state(new SendRequestSendingState())
                        .build();
                TargetUploadReportCreateContext context = contextBuilder.sendRequest(sendRequest).build();

                InvalidSendRequestStateException expect = InvalidSendRequestStateException.of(SendRequestErrorCode.SEND_REQUEST_SENDING);

                assertThatThrownBy(() -> TargetUploadReport.of(context))
                        .isInstanceOf(expect.getClass())
                        .hasMessage(expect.getMessage());
            }

            @Test
            @DisplayName("발송 요청 상태가 COMPLETED이면, 예외가 발생한다.")
            void shouldThrowException_whenSendRequestStateIsCOMPLETED() {
                SendRequest sendRequest = sendRequestBuilder.state(new SendRequestCompleteState())
                        .build();

                TargetUploadReportCreateContext context = contextBuilder.sendRequest(sendRequest).build();

                InvalidSendRequestStateException expect = InvalidSendRequestStateException.of(SendRequestErrorCode.SEND_REQUEST_COMPLETED);

                assertThatThrownBy(() -> TargetUploadReport.of(context))
                        .isInstanceOf(expect.getClass())
                        .hasMessage(expect.getMessage());
            }

            @Test
            @DisplayName("발송 요청 상태가 ERROR 이면, 예외가 발생한다.")
            void shouldThrowException_whenSendRequestStateIsError() {
                SendRequest sendRequest = sendRequestBuilder.state(new SendRequestFailState())
                        .build();
                TargetUploadReportCreateContext context = contextBuilder.sendRequest(sendRequest).build();

                InvalidSendRequestStateException expect = InvalidSendRequestStateException.of(SendRequestErrorCode.SEND_REQUEST_FAILED);

                assertThatThrownBy(() -> TargetUploadReport.of(context))
                        .isInstanceOf(expect.getClass())
                        .hasMessage(expect.getMessage());
            }

            @Test
            @DisplayName("발송 요청 상태가 CREATE이면 WAITING으로 변경된다.")
            void shouldChangeStateToWaiting_whenSendRequestStateIsCreate() {
                SendRequest sendRequest = sendRequestBuilder.state(new SendRequestCreateState())
                        .build();
                TargetUploadReportCreateContext context = contextBuilder.sendRequest(sendRequest).build();

                TargetUploadReport report = TargetUploadReport.of(context);

                assertThat(report.getState().getCurrentCode()).isEqualTo(TargetUploadStatus.WAITING);
            }

            @Test
            @DisplayName("발송 요청 상태가 HOLDING이면 WAITING으로 변경된다.")
            void shouldChangeStateToWaiting_whenSendRequestStateIsHolding() {
                SendRequest sendRequest = sendRequestBuilder.state(new SendRequestHoldingState())
                        .build();
                TargetUploadReportCreateContext context = contextBuilder.sendRequest(sendRequest).build();

                TargetUploadReport report = TargetUploadReport.of(context);

                assertThat(report.getState().getCurrentCode()).isEqualTo(TargetUploadStatus.WAITING);
            }

            @Test
            @DisplayName("발송 요청 상태가 READY이면 WAITING으로 변경된다.")
            void shouldChangeStateToWaiting_whenSendRequestStateIsReady() {
                SendRequest sendRequest = sendRequestBuilder.state(new SendRequestReadyState())
                        .build();
                TargetUploadReportCreateContext context = contextBuilder.sendRequest(sendRequest).build();

                TargetUploadReport report = TargetUploadReport.of(context);

                assertThat(report.getState().getCurrentCode()).isEqualTo(TargetUploadStatus.WAITING);
            }
        }

        @Nested
        @DisplayName("JSON 업로드 유형일 때")
        class WhenUploadTypeIsJson {

            @BeforeEach
            void setUp() {
                contextBuilder = contextBuilder.uploadType(TargetUploadType.JSON);
            }

            @Test
            @DisplayName("업로드 포맷은 저장되지 않는다.")
            void shouldNotSetUploadFormat() {
                TargetUploadReportCreateContext context = contextBuilder.build();

                TargetUploadReport report = TargetUploadReport.of(context);

                assertThat(report.getUploadFormat()).isNull();
            }

            @Test
            @DisplayName("업로드 키는 생성되지 않는다.")
            void shouldNotCreateUploadKey() {
                TargetUploadReportCreateContext context = contextBuilder.build();

                TargetUploadReport report = TargetUploadReport.of(context);

                assertThat(report.getUploadKey()).isNull();
            }
        }

        @Nested
        @DisplayName("파일 업로드 유형일 때")
        class WhenUploadTypeIsFile {
            private final String uploadKeyPrefix = "/target/upload";
            @BeforeEach
            void setUp() {
                contextBuilder.uploadType(TargetUploadType.FILE)
                        .uploadKeyPrefix(uploadKeyPrefix);
            }

            @Test
            @DisplayName("업로드 키가 생성된다.")
            void shouldGenerateUploadKey() {
                TargetUploadReportCreateContext context = contextBuilder.build();

                TargetUploadReport report = TargetUploadReport.of(context);

                assertThat(report.getUploadKey()).isNotEmpty();
            }

            @Test
            @DisplayName("업로드 키의 파일명은 ID와 업로드 포맷으로 생성된다.")
            void shouldGenerateUploadKeyAccordingToRule() {
                TargetUploadReportCreateContext context = contextBuilder
                        .uploadFormat(EnumMapperValue.fromEnumMapperType(TargetUploadFormatEnum.EXCEL))
                        .channelType(ChannelType.EMAIL)
                        .uploadKeyPrefix(uploadKeyPrefix)
                        .build();

                TargetUploadReport report = TargetUploadReport.of(context);
                String expect = "%s/%s.xlsx".formatted(uploadKeyPrefix, report.getId().toString());

                assertThat(report.getUploadKey()).isEqualTo(expect);
            }

            @Test
            @DisplayName("UPLOAD_FORMAT이 존재하지 않으면 CSV를 반환한다.")
            void shouldReturnCsv_whenUploadFormatDoesNotExist() {
                TargetUploadReportCreateContext context = contextBuilder.uploadFormat(null).build();

                TargetUploadReport report = TargetUploadReport.of(context);

                assertThat(report.getUploadFormat()).isEqualTo(TargetUploadFormatEnum.CSV);
            }
        }
    }

    @Nested
    @DisplayName("대상자 업로드 요청")
    class WhenTargetUploadRequest {
        private TargetUploadReportEntityBuilder builder;

        @BeforeEach
        void setUp() {
            builder = TargetUploadReportEntityBuilder.builder()
                    .uploadType(TargetUploadType.FILE)
                    .uploadStatus(new TargetUploadWaitingState());
        }

        @Test
        @DisplayName("업로드 상태가 CREATE이면 예외가 발생한다.")
        void shouldThrowException_whenStateIsCreate() {
            TargetUploadReport uploadReport = builder.uploadStatus(new TargetUploadCreateState())
                    .build();

            InvalidTargetUploadStateException expect = InvalidTargetUploadStateException.of(TargetUploadStatus.CREATED, TargetUploadStatus.REQUEST);

            assertThatThrownBy(() -> uploadReport.requestTargetUpload())
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("업로드 상태가 WAITING이면 REQUEST로 변경된다.")
        void shouldChangeToRequest_whenStateIsWaiting() {
            TargetUploadReport uploadReport = builder.uploadStatus(new TargetUploadWaitingState())
                    .build();

            uploadReport.requestTargetUpload();

            assertThat(uploadReport.getState().getCurrentCode())
                    .isEqualTo(TargetUploadStatus.REQUEST);
        }

        @Test
        @DisplayName("업로드 상태가 REQUEST이면 예외가 발생한다.")
        void shouldThrowException_whenStateIsRequest() {
            TargetUploadReport uploadReport = builder.uploadStatus(new TargetUploadRequestState())
                    .build();

            InvalidTargetUploadStateException expect = InvalidTargetUploadStateException.of(TargetUploadStatus.REQUEST, TargetUploadStatus.REQUEST);

            assertThatThrownBy(() -> uploadReport.requestTargetUpload())
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("업로드 상태가 PARSING이면 예외가 발생한다.")
        void shouldThrowException_whenStateIsParsing() {
            TargetUploadReport uploadReport = builder.uploadStatus(new TargetUploadParsingState())
                    .build();

            InvalidTargetUploadStateException expect = InvalidTargetUploadStateException.of(TargetUploadStatus.REQUEST, TargetUploadStatus.PARSING);

            assertThatThrownBy(() -> uploadReport.requestTargetUpload())
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("업로드 상태가 FAIL이면 예외가 발생한다.")
        void shouldThrowException_whenStateIsFail() {
            TargetUploadReport uploadReport = builder.uploadStatus(new TargetUploadFailState())
                    .build();

            InvalidTargetUploadStateException expect = InvalidTargetUploadStateException.of(TargetUploadStatus.FAIL, TargetUploadStatus.REQUEST);

            assertThatThrownBy(() -> uploadReport.requestTargetUpload())
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("업로드 상태가 COMPLETED이면 예외가 발생한다.")
        void shouldThrowException_whenStateIsComplete() {
            TargetUploadReport uploadReport = builder.uploadStatus(new TargetUploadCompleteState())
                    .build();

            InvalidTargetUploadStateException expect = InvalidTargetUploadStateException.of(TargetUploadStatus.COMPLETED, TargetUploadStatus.REQUEST);

            assertThatThrownBy(() -> uploadReport.requestTargetUpload())
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("발송 요청 시각이 생성된다.")
        void shouldCreateRequestedAt() {
            TargetUploadReport uploadReport = builder
                    .build();

            uploadReport.requestTargetUpload();

            assertThat(uploadReport.getRequestedAt().toLocalDate()).isEqualTo(LocalDate.now());
        }
    }
}
