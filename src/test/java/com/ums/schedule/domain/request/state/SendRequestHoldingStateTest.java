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
import com.ums.schedule.domain.request.exception.state.SendRequestHoldingStateException;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadDomainFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SendRequestHoldingStateTest {
    @Test
    @DisplayName("TargetUploadRequestedEvent가 발행되면, 익셉션이 발생한다.")
    void shouldReturnThrowException_whenTargetUploadRequestedEventPublished() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

        TargetUploadRequestedEvent given = TargetUploadRequestedEvent.of(targetUpload);
        SendRequestEvent event = SendRequestEvent.of(targetUpload.getSendRequest(), given);

        SendRequestState state = new SendRequestHoldingState();
        SendRequestHoldingStateException expect = SendRequestHoldingStateException.of(SendRequestStatusEnum.HOLDING);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("TargetUploadCompletedEvent가 발행되면 Status는 READY가 반환된다.")
    void shouldReturnStatusIsReady_whenTargetUploadCompletedEventPublished() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

        TargetUploadCompletedEvent given = TargetUploadCompletedEvent.of(targetUpload);
        SendRequestEvent event = SendRequestEvent.of(targetUpload.getSendRequest(), given);
        SendRequestState state = new SendRequestHoldingState();
        SendRequestState expect = state.onEvent(event);

        assertThat(expect).isInstanceOf(expect.getClass());
        assertThat(expect.currentSendRequestStatus()).isEqualTo(SendRequestStatusEnum.READY);
    }

    @Test
    @DisplayName("SendRequestedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestedEventPublished() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();

        SendRequestState state = new SendRequestHoldingState();
        SendRequestedEvent given = SendRequestedEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);
        SendRequestHoldingStateException expect =
                SendRequestHoldingStateException.of(SendRequestStatusEnum.REQUEST);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("SendRequestScheduledEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestScheduledEventPublished() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();

        SendRequestState state = new SendRequestHoldingState();
        SendRequestScheduledEvent given = SendRequestScheduledEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);
        SendRequestHoldingStateException expect = SendRequestHoldingStateException.of(given.getRequestStatus());

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("SendRequestStartedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestStartedEventPublished(){
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();

        SendRequestState state = new SendRequestHoldingState();
        SendRequestStartedEvent given = SendRequestStartedEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);

        SendRequestHoldingStateException expect = SendRequestHoldingStateException.of(SendRequestStatusEnum.SENDING);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("SendRequestCompletedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestCompletedEventPublished() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();

        SendRequestState state = new SendRequestHoldingState();
        SendRequestCompletedEvent given = SendRequestCompletedEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);

        SendRequestHoldingStateException expect = SendRequestHoldingStateException.of(SendRequestStatusEnum.COMPLETED);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("에러 메소드 호출 시 Status는 ERROR를 반환된다.")
    void shouldReturnStatusError_whenErrorMethodCalled() {
        SendRequestHoldingState state = new SendRequestHoldingState();

        SendRequestState expect = state.toError();

        assertThat(expect).isInstanceOf(SendRequestErrorState.class);
        assertThat(expect.currentSendRequestStatus()).isEqualTo(SendRequestStatusEnum.ERROR);
    }
}