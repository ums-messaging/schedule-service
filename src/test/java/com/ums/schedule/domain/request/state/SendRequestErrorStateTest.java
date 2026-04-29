package com.ums.schedule.domain.request.state;

import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestDomainFixture;
import com.ums.schedule.domain.request.SendRequestEvent;
import com.ums.schedule.domain.request.event.SendRequestCompletedEvent;
import com.ums.schedule.domain.request.event.SendRequestScheduledEvent;
import com.ums.schedule.domain.request.event.SendRequestStartedEvent;
import com.ums.schedule.domain.request.event.SendRequestedEvent;
import com.ums.schedule.domain.request.exception.state.SendRequestErrorStateException;
import com.ums.schedule.domain.target.event.TargetUploadCompletedEvent;
import com.ums.schedule.domain.target.event.TargetUploadRequestedEvent;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadDomainFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SendRequestErrorStateTest {
    @Test
    @DisplayName("TargetUploadRequestedEvent가 발행되면 Status는 HOLDING이 반환된다.")
    void shouldReturnStatusHolding_whenTargetUploadRequestedEventPublished() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

        SendRequestErrorState state = new SendRequestErrorState();
        TargetUploadRequestedEvent given = TargetUploadRequestedEvent.of(targetUpload);
        SendRequestEvent event = SendRequestEvent.of(targetUpload.getSendRequest(), given);

        SendRequestState expect = state.onEvent(event);

        assertThat(expect).isInstanceOf(SendRequestHoldingState.class);
        assertThat(expect.currentSendRequestStatus()).isEqualTo(SendRequestStatusEnum.HOLDING);
    }

    @Test
    @DisplayName("TargetUploadCompletedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadCompletedEventPublished() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

        SendRequestErrorState state = new SendRequestErrorState();
        TargetUploadCompletedEvent given = TargetUploadCompletedEvent.of(targetUpload);
        SendRequestEvent event = SendRequestEvent.of(targetUpload.getSendRequest(), given);
        SendRequestErrorStateException expect = SendRequestErrorStateException.of(SendRequestStatusEnum.READY);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("SendRequestedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestedEventPublished() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();

        SendRequestErrorState state = new SendRequestErrorState();
        SendRequestedEvent given = SendRequestedEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);

        SendRequestErrorStateException expect = SendRequestErrorStateException.of(SendRequestStatusEnum.REQUEST);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("SendRequestSendStartedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestSendStartedEventPublished() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();

        SendRequestErrorState state = new SendRequestErrorState();
        SendRequestStartedEvent given = SendRequestStartedEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);

        SendRequestErrorStateException expect = SendRequestErrorStateException.of(given.getRequestStatus());

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("SendRequestScheduledEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestScheduledEvent() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();

        SendRequestErrorState state = new SendRequestErrorState();
        SendRequestScheduledEvent given = SendRequestScheduledEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);

        SendRequestErrorStateException expect = SendRequestErrorStateException.of(SendRequestStatusEnum.SCHEDULED);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("SendRequestSendCompletedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestSendCompletedEventPublished() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();

        SendRequestErrorState state = new SendRequestErrorState();
        SendRequestCompletedEvent given = SendRequestCompletedEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);

        SendRequestErrorStateException expect = SendRequestErrorStateException.of(SendRequestStatusEnum.COMPLETED);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("에러 메소드 호출 시 익셉션이 발생한다.")
    void shouldThrowException_whenErrorMethodCalled() {
        SendRequestErrorState state = new SendRequestErrorState();

        SendRequestErrorStateException expect = SendRequestErrorStateException.of(SendRequestStatusEnum.ERROR);

        assertThatThrownBy(() -> state.toError())
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }
}