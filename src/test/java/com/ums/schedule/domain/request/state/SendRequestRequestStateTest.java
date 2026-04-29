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
import com.ums.schedule.domain.request.exception.state.SendRequestRequestStateException;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadDomainFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SendRequestRequestStateTest {

    @Test
    @DisplayName("TargetUploadRequestedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadRequestedEventPublished() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

        SendRequestRequestState state = new SendRequestRequestState();
        TargetUploadRequestedEvent given = TargetUploadRequestedEvent.of(targetUpload);
        SendRequestEvent event = SendRequestEvent.of(targetUpload.getSendRequest(), given);

        SendRequestRequestStateException expect = SendRequestRequestStateException.of(SendRequestStatusEnum.HOLDING);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("TargetUploadCompletedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadCompletedEventPublished() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

        SendRequestRequestState state = new SendRequestRequestState();
        TargetUploadCompletedEvent given = TargetUploadCompletedEvent.of(targetUpload);
        SendRequestEvent event = SendRequestEvent.of(targetUpload.getSendRequest(), given);

        SendRequestRequestStateException expect = SendRequestRequestStateException.of(SendRequestStatusEnum.READY);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("SendRequestedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestedEventPublished() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
        SendRequestRequestState state = new SendRequestRequestState();

        SendRequestedEvent given = SendRequestedEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);

        SendRequestRequestStateException expect = SendRequestRequestStateException.of(SendRequestStatusEnum.REQUEST);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("SendRequestScheduledEvent가 발행되면 SCHEDULED가 반환된다.")
    void shouldReturnStatusIsScheduled_whenSendRequestScheduledEventPublished(){
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
        SendRequestRequestState state = new SendRequestRequestState();

        SendRequestScheduledEvent given = SendRequestScheduledEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);

        SendRequestState expect = state.onEvent(event);

        assertThat(expect).isInstanceOf(SendRequestScheduleState.class);
        assertThat(expect.currentSendRequestStatus()).isEqualTo(SendRequestStatusEnum.SCHEDULED);
    }

    @Test
    @DisplayName("SendRequestSendStartedEvent가 발행되면 익셉션이 발생힌다.")
    void shouldThrowException_whenSendRequestSendStartedEvent_whenSendRequestSendStartedEvent() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
        SendRequestRequestState state = new SendRequestRequestState();

        SendRequestStartedEvent given = SendRequestStartedEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);

        SendRequestRequestStateException expect = SendRequestRequestStateException.of(SendRequestStatusEnum.SENDING);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("SendRequestSendCompletedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestSendCompletedEventPublished() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
        SendRequestRequestState state = new SendRequestRequestState();

        SendRequestCompletedEvent given = SendRequestCompletedEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);

        SendRequestRequestStateException expect = SendRequestRequestStateException.of(SendRequestStatusEnum.COMPLETED);

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