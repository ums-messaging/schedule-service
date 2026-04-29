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
import com.ums.schedule.domain.request.exception.state.SendRequestCreateStateException;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadDomainFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SendRequestCreateStateTest {

    @Test
    @DisplayName("TargetUploadRequestEvent가 발행되면, Status는 HOLDING이 반환된다.")
    void shouldReturnStatusIsHolding_whenTargetUploadRequestEventPublished() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
        TargetUploadRequestedEvent given = TargetUploadRequestedEvent.of(targetUpload);
        SendRequestEvent event = SendRequestEvent.of(targetUpload.getSendRequest(), given);

        SendRequestCreateState state = new SendRequestCreateState();
        SendRequestState expect = state.onEvent(event);

        assertThat(expect).isInstanceOf(SendRequestHoldingState.class);
        assertThat(expect.currentSendRequestStatus()).isEqualTo(SendRequestStatusEnum.HOLDING);
    }

    @Test
    @DisplayName("TargetUploadCompletedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadCompletedEventPublished() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
        TargetUploadCompletedEvent given = TargetUploadCompletedEvent.of(targetUpload);

        SendRequestEvent event = SendRequestEvent.of(targetUpload.getSendRequest(), given);
        SendRequestCreateState state = new SendRequestCreateState();

        SendRequestCreateStateException expect =
                SendRequestCreateStateException.of(SendRequestStatusEnum.READY);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());

    }

    @Test
    @DisplayName("SendRequestedEvent가 발행되면, 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestedEventPublished() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();

        SendRequestedEvent given = SendRequestedEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);
        SendRequestCreateState state = new SendRequestCreateState();

        SendRequestCreateStateException expect = SendRequestCreateStateException.of(SendRequestStatusEnum.REQUEST);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("SendRequestedScheduledEvent가 발행되면, 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestedScheduledEventPublished() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();
        SendRequestScheduledEvent given = SendRequestScheduledEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);

        SendRequestCreateState state = new SendRequestCreateState();
        SendRequestCreateStateException expect = SendRequestCreateStateException.of(SendRequestStatusEnum.SCHEDULED);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("SendRequestStartedEvent가 발행되면, 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestStartedEventPublished() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();

        SendRequestStartedEvent given = SendRequestStartedEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);
        SendRequestCreateState state = new SendRequestCreateState();

        SendRequestCreateStateException expect = SendRequestCreateStateException.of(SendRequestStatusEnum.SENDING);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("SendRequestCompletedEvent가 발행되면, 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestCompletedEventPublished() {
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest();

        SendRequestCompletedEvent given = SendRequestCompletedEvent.of(sendRequest);
        SendRequestEvent event = SendRequestEvent.of(sendRequest, given);
        SendRequestCreateState state = new SendRequestCreateState();

        SendRequestCreateStateException expect = SendRequestCreateStateException.of(SendRequestStatusEnum.COMPLETED);

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("에러 메소드 호출 시 Status는 ERROR를 반환된다.")
    void shouldReturnStatusError_whenErrorMethodCalled() {
        SendRequestCreateState state = new SendRequestCreateState();

        SendRequestState expect = state.toError();

        assertThat(expect).isInstanceOf(SendRequestErrorState.class);
        assertThat(expect.currentSendRequestStatus()).isEqualTo(SendRequestStatusEnum.ERROR);
    }
}