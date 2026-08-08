package com.ums.schedule.domain.request;

import com.ums.schedule.application.sendrequest.command.SendRequestUpdateCommand;
import com.ums.schedule.application.ums.common.request.model.SendRequestCreateContext;
import com.ums.schedule.common.code.api.ScheduleErrorCode;
import com.ums.schedule.common.code.api.SendRequestErrorCode;
import com.ums.schedule.common.code.schedule.ScheduleState;
import com.ums.schedule.domain.request.exception.InvalidSendRequestStateException;
import com.ums.schedule.domain.request.exception.SendRequestDomainException;
import com.ums.schedule.domain.schedule.exception.InvalidSchedulePeriodException;
import com.ums.schedule.domain.schedule.exception.InvalidScheduleStateException;
import com.ums.schedule.domain.schedule.policy.SchedulePeriod;
import com.ums.schedule.fixture.schedule.SchedulePeriodEntityBuilder;
import com.ums.schedule.domain.schedule.state.ScheduleInActiveStatus;
import com.ums.schedule.common.code.request.SendRequestEvent;
import com.ums.schedule.common.code.request.SendRequestStatus;
import com.ums.schedule.domain.request.customer.CustomerRequestKey;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.request.state.*;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.fixture.entity.ScheduleEntityBuilder;
import com.ums.schedule.domain.schedule.state.ScheduleActiveStatus;
import com.ums.schedule.domain.target.upload.state.TargetUploadCompleteState;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.fixture.entity.TargetUploadReportEntityBuilder;
import com.ums.schedule.fixture.sendrequest.SendRequestCreateContextBuilder;
import com.ums.schedule.fixture.entity.SendRequestEntityBuilder;
import com.ums.schedule.fixture.sendrequest.SendRequestField;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

public class SendRequestDomainTest {
    private SendRequestCreateContextBuilder contextBuilder;
    private ScheduleEntityBuilder scheduleBuilder;

    @BeforeEach
    void setUp() {
        SendMessage sendMessage = mock(SendMessage.class);
        CustomerRequestKey customerRequestKey = givenCustomerRequestKey();
        Schedule schedule = givenSchedule();
        contextBuilder = SendRequestCreateContextBuilder
                .builder()
                .customerKey(customerRequestKey)
                .schedule(schedule)
                .sendMessage(sendMessage);
    }

    private Schedule givenSchedule() {
        SchedulePeriod schedulePeriod = SchedulePeriodEntityBuilder.builder()
                .scheduleStartAt(LocalDate.now())
                .scheduleEndAt(LocalDate.now().plusMonths(1))
                .build();
        scheduleBuilder = ScheduleEntityBuilder.builder()
                .status(new ScheduleActiveStatus())
                .schedulePeriod(schedulePeriod);
        return scheduleBuilder.build();
    }

    private CustomerRequestKey givenCustomerRequestKey() {
        String customerId = String.valueOf(SendRequestField.CUSTOMER_ID.getGivenValue());
        String customerKey = String.valueOf(SendRequestField.CUSTOMER_REQUEST_ID.getGivenValue());
        return new CustomerRequestKey(customerId, customerKey);
    }

    @Nested
    @DisplayName("발송 요청 생성")
    class whenSendRequestCreate {

        @Test
        @DisplayName("발송 상태는 CREATE이다.")
        void shouldReturnCreate() {
            SendRequestCreateContext context = contextBuilder.build();

            SendRequest givenSendRequest = SendRequest.of(context);

            assertThat(givenSendRequest.getState()).isInstanceOf(SendRequestCreateState.class);
        }

        @Test
        @DisplayName("발송 요청 생성 시각이 생성된다.")
        void shouldCreateCreatedAt() {
            SendRequestCreateContext context = contextBuilder.build();

            SendRequest givenRequest =  SendRequest.of(context);

            assertThat(givenRequest.getCreatedAt().toLocalDate())
                    .isEqualTo(LocalDate.now());
        }

        @Test
        @DisplayName("고객 요청 키가 존재하지 않을 경우, 예외가 발생한다.")
        void shouldThrowException_whenCustomerRequestKeyDoesNotExist() {
            SendRequestCreateContext context = contextBuilder
                    .customerKey(null)
                    .build();

            SendRequestDomainException expect = SendRequestDomainException.of(SendRequestErrorCode.DUPLICATED_CUSTOMER_KEY);

            assertThatThrownBy(() -> SendRequest.of(context))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("만료된 스케쥴이면 예외가 발생한다.")
        void shouldThrowException_whenExpiredSchedule() {
            SchedulePeriod schedulePeriod = SchedulePeriodEntityBuilder.builder()
                    .scheduleStartAt(LocalDate.now().minusMonths(1))
                    .scheduleEndAt(LocalDate.now().minusWeeks(1)).build();
            Schedule schedule = scheduleBuilder.schedulePeriod(schedulePeriod).build();

            SendRequestCreateContext context = contextBuilder
                    .schedule(schedule)
                    .build();

            InvalidSchedulePeriodException expect = InvalidSchedulePeriodException.of(schedule.getId(), ScheduleErrorCode.EXPIRED_SCHEDULE);

            assertThatThrownBy(() -> SendRequest.of(context))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("비활성화 된 스케쥴이면 예외가 발생한다.")
        void shouldThrowException_whenInActiveSchedule() {
            Schedule schedule = scheduleBuilder.status(new ScheduleInActiveStatus()).build();
            SendRequestCreateContext context = contextBuilder
                    .schedule(schedule)
                    .build();

            InvalidScheduleStateException expect =
                    InvalidScheduleStateException.of(ScheduleState.INACTIVE, ScheduleState.ACTIVE);

            assertThatThrownBy(() -> SendRequest.of(context))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Nested
        @DisplayName("NullPointerException이 발생할 때 ")
        class WhenThrowNullPointerException {
            @Test
            @DisplayName("채널 타입이 존재하지 않으면 예외가 발생한다.")
            void shouldThrowException_whenChannelTypeIsNull() {
                SendRequestCreateContext context = contextBuilder.channelType(null).build();

                assertThatThrownBy(() -> SendRequest.of(context))
                        .isInstanceOf(NullPointerException.class)
                        .hasMessage("channel_type");
            }

            @Test
            @DisplayName("스케쥴이 존재하지 않으면 예외가 발생한다.")
            void shouldThrowException_whenScheduleIsNull() {
                SendRequestCreateContext context = contextBuilder.schedule(null).build();

                assertThatThrownBy(() -> SendRequest.of(context))
                        .isInstanceOf(NullPointerException.class)
                        .hasMessage("schedule");
            }

            @Test
            @DisplayName("template_key가 존재하지 않을 때")
            void shouldThrowException_whenTemplateKeyIsEmpty() {
                SendRequestCreateContext context = contextBuilder.templateKey(null).build();

                assertThatThrownBy(() -> SendRequest.of(context))
                        .isInstanceOf(NullPointerException.class)
                        .hasMessage("template_key");
            }

            @Test
            @DisplayName("발신자 키가 존재하지 않을 때")
            void shouldThrowException_whenSenderKeyIsEmpty() {
                SendRequestCreateContext context = contextBuilder.senderKey(null).build();

                assertThatThrownBy(() -> SendRequest.of(context))
                        .isInstanceOf(NullPointerException.class)
                        .hasMessage("sender_key");

            }
        }
    }

    @Disabled
    @Nested
    @DisplayName("발송 요청 수정 시")
    class whenSendRequestUpdated {
        @Test
        @DisplayName("발송 상태가 CREATE일 때 HOLDING으로 변경된다.")
        void shouldChangeStateToHolding_whenStateIsCreate() {
            SendRequestUpdateCommand command = SendRequestEntityBuilder.builder().toUpdateCommand();

            SendRequest givenRequest = SendRequestEntityBuilder
                    .builder()
                    .state(new SendRequestCreateState())
                    .build();

            SendRequest expect = givenRequest.updateSendRequest(null, null, command);

            assertThat(expect.getState().getCurrentCode()).isEqualTo(SendRequestStatus.HOLDING);
        }

        @Test
        @DisplayName("발송 상태가 HOLDING일 때 HOLDING으로 변경된다.")
        void shouldChangeStateToHolding_whenStateIsHolding() {
            SendRequestUpdateCommand command = SendRequestEntityBuilder.builder().toUpdateCommand();

            SendRequest givenRequest = SendRequestEntityBuilder
                    .builder()
                    .state(new SendRequestHoldingState())
                    .build();

            SendRequest expect = givenRequest.updateSendRequest(null, null, command);

            assertThat(expect.getState().getCurrentCode()).isEqualTo(SendRequestStatus.HOLDING);
        }

        @Test
        @DisplayName("발송 상태가 READY 일 때 HOLDING으로 변경된다.")
        void shouldChangeStateIsReady_whenStateIsReady() {
            SendRequestUpdateCommand command = SendRequestEntityBuilder.builder().toUpdateCommand();

            SendRequest givenRequest = SendRequestEntityBuilder
                    .builder()
                    .state(new SendRequestReadyState())
                    .build();

            SendRequest expect = givenRequest.updateSendRequest(null, null, command);

            assertThat(expect.getState().getCurrentCode()).isEqualTo(SendRequestStatus.HOLDING);
        }
        @Test
        @DisplayName("발송 요청 상태가 REQUEST 일 경우 예외가 발생한다.")
        void shouldThrowException_whenStateIsRequest() {
            SendRequestUpdateCommand command = SendRequestEntityBuilder.builder().toUpdateCommand();

            SendRequest givenRequest = SendRequestEntityBuilder
                    .builder().state(new SendRequestRequestState()).build();

            InvalidSendRequestStateException expect =
                    InvalidSendRequestStateException.of(SendRequestStatus.REQUEST, SendRequestStatus.HOLDING);

            assertThatThrownBy(() -> givenRequest.updateSendRequest(null, null, command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("발송 요청 상태가 SENDING일 경우 예외가 발생한다.")
        void shouldThrowException_whenStateIsSending() {
            SendRequestUpdateCommand command = SendRequestEntityBuilder.builder().toUpdateCommand();

            SendRequest givenRequest = SendRequestEntityBuilder
                    .builder().state(new SendRequestSendingState()).build();

            InvalidSendRequestStateException expect = InvalidSendRequestStateException.of(SendRequestStatus.SENDING, SendRequestStatus.HOLDING);

            assertThatThrownBy(() -> givenRequest.updateSendRequest(null, null, command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("발송 요청 상태가 COMPLETED일 경우 예외가 발생한다.")
        void shouldThrowException_whenStateIsCompleted() {
            SendRequestUpdateCommand command = SendRequestEntityBuilder.builder().toUpdateCommand();

            SendRequest givenRequest = SendRequestEntityBuilder
                    .builder().state(new SendRequestCompleteState()).build();

            InvalidSendRequestStateException expect = InvalidSendRequestStateException.of(SendRequestStatus.COMPLETED, SendRequestStatus.HOLDING);

            assertThatThrownBy(() -> givenRequest.updateSendRequest(null, null, command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("발송 요청 상태가 CANCEL일 때 예외가 발생한다.")
        void shouldThrowException_whenStateIsCancel() {
            SendRequestUpdateCommand command = SendRequestEntityBuilder.builder().toUpdateCommand();

            SendRequest givenRequest = SendRequestEntityBuilder
                    .builder().state(new SendRequestCancelState()).build();

            InvalidSendRequestStateException expect = InvalidSendRequestStateException.of(SendRequestStatus.CANCEL, SendRequestStatus.HOLDING);

            assertThatThrownBy(() -> givenRequest.updateSendRequest(null, null, command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("발송 요청 상태가 FAIL일 때 HOLDING으로 변경된다.")
        void shouldChangeStateToHolding_whenStateIsError() {
            SendRequestUpdateCommand command = SendRequestEntityBuilder.builder().toUpdateCommand();

            SendRequest givenRequest = SendRequestEntityBuilder
                    .builder()
                    .state(new SendRequestFailState())
                    .build();

            SendRequest expect = givenRequest.updateSendRequest(null, null, command);

            assertThat(expect.getState().getCurrentCode()).isEqualTo(SendRequestStatus.HOLDING);
        }

        @Test
        @DisplayName("발송 상태가 PAUSE 일 때 예외가 발생한다.")
        void shouldThrowException_whenStateIsPause() {
            SendRequestUpdateCommand command = SendRequestEntityBuilder.builder().toUpdateCommand();

            SendRequest givenRequest = SendRequestEntityBuilder
                    .builder().state(new SendRequestPauseState()).build();

            InvalidSendRequestStateException expect = InvalidSendRequestStateException.of(SendRequestStatus.PAUSE, SendRequestStatus.HOLDING);

            assertThatThrownBy(() -> givenRequest.updateSendRequest(null, null, command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("대상자 업로드 리포트 상태가 COMPLETED이면 상태는 READY로 변경된다.")
        void shouldChangeStateToReady() {
            SendRequestUpdateCommand command = SendRequestEntityBuilder.builder().toUpdateCommand();
            TargetUploadReport targetUpload = TargetUploadReportEntityBuilder.builder()
                    .uploadStatus(new TargetUploadCompleteState())
                    .build();
            SendRequest givenRequest = SendRequestEntityBuilder
                    .builder()
                    .build();

            SendRequest expect = givenRequest.updateSendRequest(null, targetUpload, command);

            assertThat(expect.getState().getCurrentCode()).isEqualTo(SendRequestStatus.READY);
        }


        // 비즈니스 로직
        @Test
        @DisplayName("대상자 업로드 리포트 상태가 ERROR 이면 새로운 대상자 리포트가 생성된다.")
        void shouldCreateTargetUploadReport_whenTargetUploadReportStateIsError() {

        }

        @Test
        @DisplayName("SendRequestUpdatedEvent가 발행된다.")
        void shouldPublishSendRequestUpdatedEvent() {
            SendRequestUpdateCommand command = SendRequestEntityBuilder.builder().toUpdateCommand();
            SendRequest givenRequest = SendRequestEntityBuilder
                    .builder()
                    .build();

            SendRequest expect = givenRequest.updateSendRequest(null, null, command);

            assertThat(expect.getEvent()).isEqualTo(SendRequestEvent.SEND_REQUEST_UPDATED);
        }

        @Test
        @DisplayName("대상자 업로드 리포트 상태가 COMPLETED이면 상태는 SendRequestReadyEvent가 발행된다.")
        void shouldPublishSendRequestReadyEvent_whenTargetUploadReportIsCompleted() {
            SendRequestUpdateCommand command = SendRequestEntityBuilder.builder().toUpdateCommand();
            TargetUploadReport targetUpload = TargetUploadReportEntityBuilder.builder()
                    .uploadStatus(new TargetUploadCompleteState())
                    .build();
            SendRequest givenRequest = SendRequestEntityBuilder
                    .builder()
                    .build();

            SendRequest expect = givenRequest.updateSendRequest(null, targetUpload, command);

            assertThat(expect.getEvent()).isEqualTo(SendRequestEvent.SEND_REQUEST_READY);
        }

        @Test
        @DisplayName("새로운 Schedule이 NULL이면 기존 스케쥴이 반환된다.")
        void shouldReturnSchedule_whenScheduleIsNull() {
            SendRequestUpdateCommand command = SendRequestEntityBuilder.builder().toUpdateCommand();

            SendRequest givenRequest = SendRequestEntityBuilder
                    .builder()
                    .build();

            SendRequest expect = givenRequest.updateSendRequest(null, null, command);

            assertThat(expect.getSchedule()).isNotNull();
            assertThat(expect.getSchedule()).isEqualTo(givenRequest.getSchedule());
        }

        @Test
        @DisplayName("새로운 스케쥴을 입력하면 입력한 스케쥴을 반환한다.")
        void shouldReturnNewSchedule_whenScheduleIsNotNull(){
            SendRequestUpdateCommand command = SendRequestEntityBuilder.builder().toUpdateCommand();

            SendRequest givenRequest = SendRequestEntityBuilder
                    .builder()
                    .build();

            Schedule givenNewSchedule = ScheduleEntityBuilder.builder().build();
            SendRequest expect = givenRequest.updateSendRequest(givenNewSchedule, null, command);

            assertThat(expect.getSchedule()).isNotNull();
            assertThat(expect.getSchedule()).isEqualTo(givenNewSchedule);
        }

        @Test
        @DisplayName("새로운 대상자 업로드 리포트가 NULL이면 기존 대상자 업로드 리포트가 반환된다. ")
        void shouldReturnTargetUploadReport_whenNewTargetUploadIsNull() {
            SendRequestUpdateCommand command = SendRequestEntityBuilder.builder().toUpdateCommand();

            SendRequest givenRequest = SendRequestEntityBuilder
                    .builder()
                    .build();

            SendRequest expect = givenRequest.updateSendRequest(null, null, command);

            assertThat(expect.getCurrentTargetUpload()).isNotNull();
            assertThat(expect.getCurrentTargetUpload()).isEqualTo(givenRequest.getCurrentTargetUpload());
        }

        @Test
        @DisplayName("새로운 대상자 업로드 리포트를 입력하면 새로운 대상자 업로드 리포트가 반환된다. ")
        void shouldReturnNewTargetUploadReport_whenNewTargetUploadIsNull() {
            SendRequestUpdateCommand command = SendRequestEntityBuilder.builder().toUpdateCommand();

            SendRequest givenRequest = SendRequestEntityBuilder
                    .builder()
                    .build();

            TargetUploadReport newTargetUpload = TargetUploadReportEntityBuilder.builder().build();
            SendRequest expect = givenRequest.updateSendRequest(null, newTargetUpload, command);

            assertThat(expect.getCurrentTargetUpload()).isNotNull();
            assertThat(expect.getCurrentTargetUpload()).isEqualTo(newTargetUpload);
        }
    }

    @Nested
    @DisplayName("발송 시작 요청 시")
    class whenSendStartRequest {

        @Test
        @DisplayName("발송 상태가 CREATE 이면 익셉션이 발생한다.")
        void shouldThrowException_whenStateIsCreate() {

        }

        @Test
        @DisplayName("발송 상태가 HOLDING이면 익셉션이 발생한다.")
        void shouldThrowException_whenStateIsHolding() {

        }

        @Test
        @DisplayName("발송 상태가 READY이면 익셉션이 발생한다.")
        void shouldThrowException_whenStateIsReady() {

        }

        @Test
        @DisplayName("발송 상태가 SENDING이면 익셉션이 발생한다.")
        void shouldThrowException_whenSendStateIsSending() {

        }

        @Test
        @DisplayName("발송 상태가 COMPLETED이면 익셉션이 발생한다.")
        void shouldThrowException_whenSendStateIsCompleted() {

        }

        @Test
        @DisplayName("발송 상태가 PAUSE이면 익셉션이 발생한다.")
        void shouldThrowException_whenSendStateIsPause() {

        }

        @Test
        @DisplayName("발송 상태가 FAIL이면 익셉션이 발생한다.")
        void shouldThrowException_whenSendStateIsFail() {

        }

        @Test
        @DisplayName("발송 상태가 RETRYING이면 익셉션이 발생한다.")
        void shouldThrowException_whenSendStateIsRetrying() {

        }

        @Test
        @DisplayName("발송 상태가 CANCEL이면 익셉션이 발생한다.")
        void shouldThrowException_whenStateIsCancel() {

        }

        @Test
        @DisplayName("대상자 목록이 존재하지 않으면 익셉션이 발생한다.")
        void shouldThrowException_whenSendTargetListDoesNotExist(){

        }

        @Test
        @DisplayName("발송 상태가 READY일 때 REQUEST로 변경된다.")
        void shouldChangeStateToRequest_whenSendStateIsReady() {

        }

        // 비즈니스 로직
        @Test
        @DisplayName("발송 요청 리포트가 생성 된다.")
        void shouldCreateSendRequestReport() {

        }

        @Test
        @DisplayName("발송 요청 리포트가 NULL이면 예외가 발생한다.")
        void shouldThrowException_whenSendReportIsNull() {

        }


        @Test
        @DisplayName("state가 READY일 때 SendRequestRequestedEvent가 발행된다.")
        void shouldPublishSendRequestSendStartedEvent() {

        }

        @Test
        @DisplayName("스케쥴이 실행 중이 아닐 때는 익셉션이 발생한다.")
        void shouldThrowException_whenScheduleStateIsNotActive() {

        }

        @Test
        @DisplayName("발송 요청 시간이 스케쥴 기간에 포함되지 않으면 익셉션이 발생한다.")
        void shouldThrowException_whenReservationDateDoesNotContainInSchedulePeriod() {

        }

        @Test
        @DisplayName("requestedAt은 현재 시각으로 생성된다.")
        void shouldReturnRequestedAtIsCurrentTime() {

        }
    }

    @Nested
    @DisplayName("발송 요청 취소 할 때")
    class whenSendRequestCancel {
        @Test
        @DisplayName("발송 요청 상태가 SENDING일 때 익셉션이 발생한다.")
        void shouldThrowException_whenSendRequestStateIsSending() {

        }

        @Test
        @DisplayName("발송 요청 상태가 COMPLETED일 때 익셉션이 발생한다.")
        void shouldThrowException_whenSendRequestStateIsCompleted() {

        }


        @Test
        @DisplayName("발송 요청 상태가 CREATE 일 때 발송 요청 상태는 CANCEL로 변경된다.")
        void shouldChangeStateToCancel_whenStateIsCreate() {

        }

        @Test
        @DisplayName("발송 요청 상태가 HOLDING일 때 발송 요청 상태는 CANCEL로 변경된다.")
        void shouldChangeStateToCancel_whenStateIsHolding() {

        }

        @Test
        @DisplayName("발송 요청 상태가 READY일 때 발송 요청 상태는 CANCEL로 변경된다.")
        void shouldChangeStateToCancel_whenStateIsReady() {

        }
        @Test
        @DisplayName("발송 요청 상태가 REQUEST일 때 발송 요청 상태는 CANCEL로 변경된다.")
        void shouldChangeStateToCancel_whenStateIsRequest() {

        }


        @Test
        @DisplayName("SendRequestSendCanceledEvent를 발행한다.")
        void shouldPublishSendRequestSendCanceledEvent() {

        }
    }

    @Nested
    @DisplayName("발송 일시중지 할 때")
    class whenSendRequestPause {
        @Test
        @DisplayName("발송 요청 상태가 SENDING인 경우에만 일시 중지 할 수 있다.")
        void shouldChangeStateToPause_whenStateIsSending() {

        }

        @Test
        @DisplayName("발송 요청 상태는 CANCEL로 변경된다.")
        void shouldChangeStateToCancel() {

        }

        @Test
        @DisplayName("SendRequestCanceledEvent를 발행한다.")
        void shouldPublishSendRequestCanceledEvent() {

        }


    }

    @Nested
    @DisplayName("발송 시작 시")
    class whenSendStart {
        @Test
        @DisplayName("발송 시작 시 JOB이 생성된다.")
        void shouldCreateSendJob() {

        }

        @Test
        @DisplayName("발송 시작 시 JOB이 NULL이면 예외가 발생한다.")
        void shouldThrowException_whenJobIsNull() {

        }

        @Test
        @DisplayName("Job생성 실패 시 발송 상태는 FAIL로 변경된다.")
        void shouldChangeStateToFail_whenJobDoesNotCreate() {

        }

        @Test
        @DisplayName("도메인 그룹 대상자가 존재하지 않으면 익셉션이 발생한다.")
        void shouldThrowException_whenDomainGroupTargetDoesNotExist() {

        }

        @Test
        @DisplayName("SendRequestSendingStartedEvent가 발행된다.")
        void shouldPublishSendRequestSendingStartedEvent() {

        }
    }

    @Nested
    @DisplayName("발송 중지 요청 시")
    class whenSendRequestStop {
        @Test
        @DisplayName("발송 상태가 SENDING이면 익셉션이 발생한다.")
        void shouldThrowException_whenSendRequestStateIsSending() {

        }
    }

    @Nested
    @DisplayName("대상자 업로드 요청 시")
    class whenTargetUploadRequest {
        @Test
        @DisplayName("템플릿이 존재하지 않으면 익셉션이 발생한다.")
        void shouldThrowException_whenTemplateDoesNotExist() {

        }

        @Test
        @DisplayName("템플릿이 존재하지 않으면 상태는 ERROR로 변경된다.")
        void shouldChangeStateToError_whenTemplateDoesNotExist() {

        }

        @Test
        @DisplayName("SendRequestUpdatedEvent가 발행된다.")
        void shouldPublishSendRequestUpdatedEvent() {

        }


    }
}
