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
import com.ums.schedule.domain.request.exception.state.SendRequestScheduleStateException;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadDomainFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SendRequestScheduledStateTest {
    @Test
    @DisplayName("TargetUploadRequestedEvent가 발행되면, 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadRequestedEventPublished() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

        SendRequestScheduleState state = new SendRequestScheduleState();
        TargetUploadRequestedEvent given = TargetUploadRequestedEvent.of(targetUpload);
        SendRequestEvent event = SendRequestEvent.of(targetUpload.getSendRequest(), given);

        SendRequestScheduleStateException expect = SendRequestScheduleStateException.of(given.getRequestStatus());

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("TargetUploadCompletedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadCompletedEventPublished() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

        SendRequestScheduleState state = new SendRequestScheduleState();
        TargetUploadCompletedEvent given = TargetUploadCompletedEvent.of(targetUpload);
        SendRequestEvent event = SendRequestEvent.of(targetUpload.getSendRequest(), given);

        SendRequestScheduleStateException expect = SendRequestScheduleStateException.of(given.getRequestStatus());

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("SendRequestedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestedEventPublished() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
        SendRequestScheduleState state = new SendRequestScheduleState();
        SendRequestedEvent given = SendRequestedEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);


        SendRequestScheduleStateException expect = SendRequestScheduleStateException.of(given.getRequestStatus());

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("ScheduledEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenScheduledEventPublished() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
        SendRequestScheduleState state = new SendRequestScheduleState();
        SendRequestScheduledEvent given = SendRequestScheduledEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);

        SendRequestScheduleStateException expect = SendRequestScheduleStateException.of(given.getRequestStatus());

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("SendRequestSendStartedEvent가 발행되면 Status는 SENDING이 반환된다.")
    void shouldReturnStatusIsSending_whenSendRequestSendStartedEventPublish() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
        SendRequestScheduleState state = new SendRequestScheduleState();
        SendRequestStartedEvent given = SendRequestStartedEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);

        SendRequestState expect = state.onEvent(event);

        assertThat(expect).isInstanceOf(SendRequestSendingState.class);
        assertThat(expect.currentSendRequestStatus()).isEqualTo(SendRequestStatusEnum.SENDING);
    }

    @Test
    @DisplayName("SendRequestSendCompletedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestSendCompletedEventPublished() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
        SendRequestScheduleState state = new SendRequestScheduleState();
        SendRequestCompletedEvent given = SendRequestCompletedEvent.of(sendRequest);

        SendRequestScheduleStateException expect = SendRequestScheduleStateException.of(given.getRequestStatus());
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("에러 메소드 호출 시 Status는 ERROR를 반환된다.")
    void shouldReturnStatusError_whenErrorMethodCalled() {
        SendRequestScheduleState state = new SendRequestScheduleState();

        SendRequestState expect = state.toError();

        assertThat(expect).isInstanceOf(SendRequestErrorState.class);
        assertThat(expect.currentSendRequestStatus()).isEqualTo(SendRequestStatusEnum.ERROR);
    }
}