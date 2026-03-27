package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.send.domain.request.status.exception.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SendCompletedStatusTest {

    @Test
    @DisplayName("COMPLETED 상태에서 READY 상태로 변경하면 에러가 발생한다.")
    void shouldReturnStatusIsReady_whenToReady() {
        SendRequestStatus status = new SendSuccessStatus();

        SendReadyStatusException result = SendReadyStatusException.of(status.currentSendRequestStatus().code());

        assertThatThrownBy(() -> status.toReady())
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("COMPLETED 상태에서 REQUEST 상태로 변경하면 에러가 발생한다.")
    void shouldThrowException_whenToRequest() {
        SendRequestStatus status = new SendSuccessStatus();

        SendRequestStatusException result = SendRequestStatusException
                .of(status.currentSendRequestStatus().code());

        assertThatThrownBy(() -> status.toRequest())
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("COMPLETED 상태에서 SCHEDULE 상태로 변경하면 에러가 발생한다.")
    void shouldThrowException_whenToScheduled() {
        SendRequestStatus status = new SendSuccessStatus();

        SchedulingStatusException result = SchedulingStatusException.of(status.currentSendRequestStatus().code());

        assertThatThrownBy(() -> status.toScheduled())
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("COMPLETED 상태에서 SENDING 상태로 변경하면 에러가 발생한다.")
    void shouldThrowException_whenToSend() {
        SendRequestStatus status = new SendSuccessStatus();

        SendingStatusException result = SendingStatusException.of(status.currentSendRequestStatus().code());

        assertThatThrownBy(() -> status.toSend())
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("COMPLETED 상태에서 COMPLETED 상태로 변경하면 에러가 발생한다.")
    void shouldThrowException_whenToCompleted() {
        SendRequestStatus status = new SendSuccessStatus();

        SendCompletedStatusException result = SendCompletedStatusException.of(status.currentSendRequestStatus().code());

        assertThatThrownBy(() -> status.toComplete())
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("COMPLETED 상태에서 ERROR 상태로 변경하면 에러가 발생한다.")
    void shouldThrowException_whenToError() {
        SendRequestStatus status = new SendSuccessStatus();

        SendErrorStatusException result = SendErrorStatusException.of(status.currentSendRequestStatus().code());

        assertThatThrownBy(() -> status.toError())
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }
}