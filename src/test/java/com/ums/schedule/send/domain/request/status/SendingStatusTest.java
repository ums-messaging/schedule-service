package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.domain.request.status.exception.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SendingStatusTest {

    @Test
    @DisplayName("SENDING 상태에서 READY 상태로 변경하면 에러가 발생한다.")
    void shouldReturnStatusIsReady_whenToReady() {
        SendRequestStatus status = new SendingStatus();

        SendReadyStatusException result = SendReadyStatusException.of(status.currentSendRequestStatus().code());

        assertThatThrownBy(() -> status.toReady())
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("SENDING 상태에서 REQUEST 상태로 변경하면 에러가 발생한다.")
    void shouldThrowException_whenToRequest() {
        SendRequestStatus status = new SendingStatus();

        SendRequestStatusException result = SendRequestStatusException
                .of(status.currentSendRequestStatus().code());

        assertThatThrownBy(() -> status.toRequest())
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("SENDING 상태에서 SCHEDULE 상태로 변경하면 에러가 발생한다.")
    void shouldThrowException_whenToScheduled() {
        SendRequestStatus status = new SendingStatus();

        SchedulingStatusException result = SchedulingStatusException.of(status.currentSendRequestStatus().code());

        assertThatThrownBy(() -> status.toScheduled())
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("SENDING 상태에서 SENDING 상태로 변경하면 에러가 발생한다.")
    void shouldThrowException_whenToSend() {
        SendRequestStatus status = new SendingStatus();

        SendingStatusException result = SendingStatusException.of(status.currentSendRequestStatus().code());

        assertThatThrownBy(() -> status.toSend())
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("SENDING 상태에서 COMPLETED 상태로 변경하면 COMPLETED가 반환된다.")
    void shouldThrowException_whenToCompleted() {
        SendRequestStatus status = new SendingStatus();

        SendRequestStatus result = status.toComplete();

        assertThat(result.currentSendRequestStatus())
                .isEqualTo(SendRequestStatusEnum.SUCCESS);
    }

    @Test
    @DisplayName("SENDING 상태에서 ERROR 상태로 변경하면 ERROR 반환돈다.")
    void shouldThrowException_whenToError() {
        SendRequestStatus status = new SendingStatus();

        SendRequestStatus result = status.toError();

        assertThat(result.currentSendRequestStatus())
                .isEqualTo(SendRequestStatusEnum.FAILED);
    }
}