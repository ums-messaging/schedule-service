package com.ums.schedule.domain.request.state;

import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.domain.request.SendRequestEvent;
import com.ums.schedule.domain.request.event.SendRequestScheduledEvent;
import com.ums.schedule.domain.request.event.SendRequestCompletedEvent;
import com.ums.schedule.domain.request.event.SendRequestedEvent;
import com.ums.schedule.domain.request.event.SendRequestStartedEvent;
import com.ums.schedule.domain.target.event.TargetUploadCompletedEvent;
import com.ums.schedule.domain.target.event.TargetUploadRequestedEvent;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestDomainFixture;
import com.ums.schedule.domain.request.exception.state.SendRequestReadyStateException;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadDomainFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.ums.schedule.code.send.SendRequestStatusEnum.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SendRequestReadyStateTest {

    @Test
    @DisplayName("TargetUploadRequestedEvent가 발행되면, Status는 PENDING이 반환된다.")
    void shouldReturnStatusIsPending_whenTargetUploadRequestedEventPublished() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
        TargetUploadRequestedEvent given = TargetUploadRequestedEvent.of(targetUpload);
        SendRequestEvent event = SendRequestEvent.of(targetUpload.getSendRequest(), given);

        SendRequestState state = new SendRequestReadyState();
        SendRequestState expect = state.onEvent(event);

        assertThat(expect).isInstanceOf(SendRequestHoldingState.class);
        assertThat(expect.currentSendRequestStatus()).isEqualTo(HOLDING);
    }

    @Test
    @DisplayName("TargetUploadCompletedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUplaodCompletedEventPublished(){
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
        TargetUploadCompletedEvent given = TargetUploadCompletedEvent.of(targetUpload);
        SendRequestEvent event = SendRequestEvent.of(targetUpload.getSendRequest(), given);

        SendRequestReadyState state = new SendRequestReadyState();
        SendRequestReadyStateException expect = SendRequestReadyStateException.of(READY);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(SendRequestReadyStateException.class)
                .hasMessage(expect.getMessage());

    }

    @Test
    @DisplayName("SendRequestedEvent가 발행되면 Status는 REQUEST가 반환된다.")
    void shouldReturnStatusIsRequest_whenSendRequestEventPublished() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();

        SendRequestReadyState state = new SendRequestReadyState();
        SendRequestedEvent given = SendRequestedEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);

        SendRequestState expect = state.onEvent(event);

        assertThat(expect).isInstanceOf(SendRequestRequestState.class);
        assertThat(expect.currentSendRequestStatus()).isEqualTo(SendRequestStatusEnum.REQUEST);
    }

    @Test
    @DisplayName("SendRequestScheduledEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestScheduledEventPublished() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();

        SendRequestReadyState state = new SendRequestReadyState();
        SendRequestScheduledEvent given = SendRequestScheduledEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);

        SendRequestReadyStateException expect = SendRequestReadyStateException.of(SCHEDULED);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("SendRequestSendStartedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestSendStartedEvent() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();

        SendRequestReadyState state = new SendRequestReadyState();
        SendRequestStartedEvent given = SendRequestStartedEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);

        SendRequestReadyStateException expect = SendRequestReadyStateException.of(SENDING);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("SendRequestSendCompletedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestSendCompletedEventPublished(){
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();

        SendRequestReadyState state = new SendRequestReadyState();
        SendRequestCompletedEvent given = SendRequestCompletedEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);

        SendRequestReadyStateException expect = SendRequestReadyStateException.of(COMPLETED);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("에러 메소드 호출 시 Status는 ERROR를 반환된다.")
    void shouldReturnStatusError_whenErrorMethodCalled() {
        SendRequestRequestState state = new SendRequestRequestState();

        SendRequestState expect = state.toError();

        assertThat(expect).isInstanceOf(SendRequestErrorState.class);
        assertThat(expect.currentSendRequestStatus()).isEqualTo(SendRequestStatusEnum.ERROR);
    }
}