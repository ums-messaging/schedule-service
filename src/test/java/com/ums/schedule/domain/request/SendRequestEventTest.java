package com.ums.schedule.domain.request;

import com.ums.schedule.code.send.ResultCodeEnum;
import com.ums.schedule.code.send.SendRequestEventTypeEnum;
import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.domain.request.event.*;
import com.ums.schedule.domain.request.exception.SendRequestException;
import com.ums.schedule.domain.request.state.*;
import com.ums.schedule.domain.target.event.TargetUploadCompletedEvent;
import com.ums.schedule.domain.target.event.TargetUploadRequestedEvent;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadDomainFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SendRequestEventTest {
    private SendEvent givenEvent;

    @Nested
    @DisplayName("TargetUploadRequestedEvent 발행 시")
    class whenTargetUploadRequestedEventPublished  {

        @BeforeEach
        void setUp() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
            givenEvent = TargetUploadRequestedEvent.of(targetUpload);
        }

        @Test
        @DisplayName("SendRequestEvent의 EventType은 TARGET_UPLOAD_REQUESTED를 반환한다.")
        void shouldReturnEventTypeTargetUploadRequested_whenSendRequestEvent() {
            SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
            SendRequestEvent expect = SendRequestEvent.of(sendRequest, givenEvent);

            assertThat(expect.getEventType())
                    .isEqualTo(SendRequestEventTypeEnum.TARGET_UPLOAD_REQUEST);
        }

        @Test
        @DisplayName("SendRequest의 상태는 HOLDING으로 변경된다.")
        void shouldChangeStatusToHolding_whenSendRequest() {
            SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();

            SendRequestEvent.of(sendRequest, givenEvent);

            assertThat(sendRequest.getState()).isInstanceOf(SendRequestHoldingState.class);
            assertThat(sendRequest.getStatus()).isEqualTo(SendRequestStatusEnum.HOLDING);
        }
    }

    @Nested
    @DisplayName("TargetUploadCompletedEvent 발행 시")
    class whenTargetUploadCompletedEventPublished {

        @Test
        @DisplayName("SendRequestEvent의 EventType은 TARGET_UPLOAD_COMPLETED를 반환한다.")
        void shouldReturnEventTypeTargetUploadCompleted_whenSendRequestEvent() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
            SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
            SendRequestEvent.of(sendRequest, TargetUploadRequestedEvent.of(targetUpload));

            givenEvent = TargetUploadCompletedEvent.of(targetUpload);
            SendRequestEvent.of(sendRequest, givenEvent);

            assertThat(givenEvent.getEventType())
                    .isEqualTo(SendRequestEventTypeEnum.TARGET_UPLOAD_COMPLETED);
        }

        @Test
        @DisplayName("SendRequest의 상태는 READY로 변경된다.")
        void shouldChangeStatusToReady_whenSendRequest() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
            SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
            SendRequestEvent.of(sendRequest, TargetUploadRequestedEvent.of(targetUpload));

            givenEvent = TargetUploadCompletedEvent.of(targetUpload);
            SendRequestEvent.of(sendRequest, givenEvent);

            assertThat(sendRequest.getState()).isInstanceOf(SendRequestReadyState.class);
            assertThat(sendRequest.getStatus()).isEqualTo(SendRequestStatusEnum.READY);
        }
    }

    @Nested
    @DisplayName("SendRequestedEvent 발행 시")
    class whenSendRequestedEventPublished {
        @Test
        @DisplayName("EventType은 SEND_REQUESTED를 반환한다.")
        void shouldReturnEventTypeSendRequested_whenSendRequestEvent() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
            SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
            SendRequestEvent.of(sendRequest, TargetUploadRequestedEvent.of(targetUpload));
            SendRequestEvent.of(sendRequest, TargetUploadCompletedEvent.of(targetUpload));

            givenEvent = SendRequestedEvent.of(sendRequest);
            SendRequestEvent expect = SendRequestEvent.of(sendRequest, givenEvent);

            assertThat(expect.getEventType()).isEqualTo(SendRequestEventTypeEnum.SEND_REQUESTED);
        }

        @Test
        @DisplayName("SendRequest의 상태는 REQUEST로 변경된다.")
        void shouldChangeStatusToRequest_whenSendRequest() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
            SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
            SendRequestEvent.of(sendRequest, TargetUploadRequestedEvent.of(targetUpload));
            SendRequestEvent.of(sendRequest, TargetUploadCompletedEvent.of(targetUpload));

            givenEvent = SendRequestedEvent.of(sendRequest);
            SendRequestEvent.of(sendRequest, givenEvent);

            assertThat(sendRequest.getState()).isInstanceOf(SendRequestRequestState.class);
            assertThat(sendRequest.getStatus()).isEqualTo(SendRequestStatusEnum.REQUEST);
        }
    }

    @Nested
    @DisplayName("ScheduledEvent 발생 시")
    class whenScheduledEventPublished {
        @Test
        @DisplayName("SendRequestEvent의 EventType은 SCHEDULED이다.")
        void shouldReturnEventTypeScheduled_whenSendRequestEvent() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
            SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
            SendRequestEvent.of(sendRequest, TargetUploadRequestedEvent.of(targetUpload));
            SendRequestEvent.of(sendRequest, TargetUploadCompletedEvent.of(targetUpload));
            SendRequestEvent.of(sendRequest, SendRequestedEvent.of(sendRequest));

            givenEvent = SendRequestScheduledEvent.of(sendRequest);
            SendRequestEvent expect = SendRequestEvent.of(sendRequest, givenEvent);

            assertThat(expect.getEventType()).isEqualTo(SendRequestEventTypeEnum.SCHEDULED);
        }

        @Test
        @DisplayName("SendRequest의 상태는 HOLDING으로 변경된다.")
        void shouldChangeStatusToHolding_whenSendRequest() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
            SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
            SendRequestEvent.of(sendRequest, TargetUploadRequestedEvent.of(targetUpload));
            SendRequestEvent.of(sendRequest, TargetUploadCompletedEvent.of(targetUpload));
            SendRequestEvent.of(sendRequest, SendRequestedEvent.of(sendRequest));

            givenEvent = SendRequestScheduledEvent.of(sendRequest);
            SendRequestEvent.of(sendRequest, givenEvent);

            assertThat(sendRequest.getState()).isInstanceOf(SendRequestScheduleState.class);
            assertThat(sendRequest.getStatus()).isEqualTo(SendRequestStatusEnum.SCHEDULED);
        }
    }

    @Nested
    @DisplayName("SendStartedEvent 발행 시")
    class whenSendStartedEventPublished {
        @Test
        @DisplayName("SendRequestEvent의 EventType은 SEND_STARTED가 반환된다.")
        void shouldReturnEventTypeSendStarted_whenSendRequestEvent() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
            SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
            SendRequestEvent.of(sendRequest, TargetUploadRequestedEvent.of(targetUpload));
            SendRequestEvent.of(sendRequest, TargetUploadCompletedEvent.of(targetUpload));
            SendRequestEvent.of(sendRequest, SendRequestedEvent.of(sendRequest));
            SendRequestEvent.of(sendRequest, SendRequestScheduledEvent.of(sendRequest));

            givenEvent = SendRequestStartedEvent.of(sendRequest);
            SendRequestEvent expect = SendRequestEvent.of(sendRequest, givenEvent);

            assertThat(expect.getEventType()).isEqualTo(SendRequestEventTypeEnum.SEND_STARTED);
        }

        @Test
        @DisplayName("SendRequest의 상태는 SENDING으로 변경된다.")
        void shouldChangeStatusToSending_whenSendRequest() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
            SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
            SendRequestEvent.of(sendRequest, TargetUploadRequestedEvent.of(targetUpload));
            SendRequestEvent.of(sendRequest, TargetUploadCompletedEvent.of(targetUpload));
            SendRequestEvent.of(sendRequest, SendRequestedEvent.of(sendRequest));
            SendRequestEvent.of(sendRequest, SendRequestScheduledEvent.of(sendRequest));

            givenEvent = SendRequestStartedEvent.of(sendRequest);
            SendRequestEvent.of(sendRequest, givenEvent);

            assertThat(sendRequest.getState()).isInstanceOf(SendRequestSendingState.class);
            assertThat(sendRequest.getStatus()).isEqualTo(SendRequestStatusEnum.SENDING);
        }
    }

    @Nested
    @DisplayName("SendCompletedEvent 발행 시")
    class whenSendCompletedEventPublished {
        @Test
        @DisplayName("SendRequestEvent의 EventType은 SEND_ENDED를 반환한다.")
        void shouldReturnEventTypeSendCompleted_whenSendRequestEvent() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
            SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
            SendRequestEvent.of(sendRequest, TargetUploadRequestedEvent.of(targetUpload));
            SendRequestEvent.of(sendRequest, TargetUploadCompletedEvent.of(targetUpload));
            SendRequestEvent.of(sendRequest, SendRequestedEvent.of(sendRequest));
            SendRequestEvent.of(sendRequest, SendRequestScheduledEvent.of(sendRequest));
            SendRequestEvent.of(sendRequest, SendRequestStartedEvent.of(sendRequest));

            givenEvent = SendRequestCompletedEvent.of(sendRequest);
            SendRequestEvent expect = SendRequestEvent.of(sendRequest, givenEvent);

            assertThat(expect.getEventType()).isEqualTo(SendRequestEventTypeEnum.SEND_ENDED);
        }

        @Test
        @DisplayName("SendRequest의 Status는 COMPLETE로 변경된다.")
        void shouldChangeStatusToComplete_whenSendRequest() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
            SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
            SendRequestEvent.of(sendRequest, TargetUploadRequestedEvent.of(targetUpload));
            SendRequestEvent.of(sendRequest, TargetUploadCompletedEvent.of(targetUpload));
            SendRequestEvent.of(sendRequest, SendRequestedEvent.of(sendRequest));
            SendRequestEvent.of(sendRequest, SendRequestScheduledEvent.of(sendRequest));
            SendRequestEvent.of(sendRequest, SendRequestStartedEvent.of(sendRequest));

            givenEvent = SendRequestCompletedEvent.of(sendRequest);
            SendRequestEvent.of(sendRequest, givenEvent);

            assertThat(sendRequest.getState()).isInstanceOf(SendRequestCompleteState.class);
            assertThat(sendRequest.getStatus()).isEqualTo(SendRequestStatusEnum.COMPLETED);
        }
    }

    @Nested
    @DisplayName("익셉션 발생 시")
    class whenSendRequestException {
        @Test
        @DisplayName("에러 발생 시 ResultCode는 FAIL이다.")
        void shouldReturnResultCodeFail_whenError() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
            SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
            TargetUploadRequestedEvent event = TargetUploadRequestedEvent.of(targetUpload);

            FakeSendRequestException exception = FakeSendRequestException.of();
            SendRequestEvent requestEvent = SendRequestEvent.of(sendRequest, event, exception);

            assertThat(requestEvent.getResultCode()).isEqualTo(ResultCodeEnum.FAIL);
            assertThat(requestEvent.getResultMessage()).isEqualTo(exception.getMessage());
        }

        @Test
        @DisplayName("에러 발생 시 SendRequest의 상태는 ERROR이다.")
        void shouldReturnSendRequestStatusIsError_whenExceptionCreate(){
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
            SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
            TargetUploadRequestedEvent event = TargetUploadRequestedEvent.of(targetUpload);

            FakeSendRequestException exception = FakeSendRequestException.of();
            SendRequestEvent requestEvent = SendRequestEvent.of(sendRequest, event, exception);

            assertThat(sendRequest.getState()).isInstanceOf(SendRequestErrorState.class);
            assertThat(sendRequest.getStatus()).isEqualTo(SendRequestStatusEnum.ERROR);
        }
    }

    @Test
    @DisplayName("Json Parse 테스트")
    void jsonParseTest() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
        TargetUploadRequestedEvent event = TargetUploadRequestedEvent.of(targetUpload);
        SendRequestEvent expect = SendRequestEvent.of(sendRequest, event);
        String result = expect.toPayload(event);

        assertThat(result).startsWith("{").endsWith("}");

    }

    static class FakeSendRequestException extends SendRequestException {
        protected FakeSendRequestException(String message) {
            super(message);
        }
        public static FakeSendRequestException of() {
            return new FakeSendRequestException("error");
        }
    }
}

