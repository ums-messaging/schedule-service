package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.domain.request.status.exception.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SendScheduledStatusTest {
    @Test
    @DisplayName("SCHEDULED 상태에서 READY 상태로 변경하면 에러가 발생한다.")
    void shouldReturnStatusIsReady_whenToReady() {
        SendRequestStatus status = new SchedulingStatus();

        SendReadyStatusException result = SendReadyStatusException.of(status.currentSendRequestStatus().code());

        assertThatThrownBy(() -> status.toReady())
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("SCHEDULED 상태에서 REQUEST 상태로 변경하면 에러가 발생한다.")
    void shouldThrowException_whenToRequest() {
        SendRequestStatus status = new SchedulingStatus();

        SendRequestStatusException result = SendRequestStatusException.of(status.currentSendRequestStatus().code());

        assertThatThrownBy(() -> status.toRequest())
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("SCHEDULED 상태에서 SCHEDULE 상태로 변경하면 에러가 발생한다.")
    void shouldThrowException_whenToScheduled() {
        SendRequestStatus status = new SchedulingStatus();

        SchedulingStatusException result = SchedulingStatusException.of(status.currentSendRequestStatus().code());

        assertThatThrownBy(() -> status.toScheduled())
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("SCHEDULED 상태에서 SEND 상태로 변경하면 SEND가 반환된다.")
    void shouldThrowException_whenToSend() {
        SendRequestStatus status = new SchedulingStatus();

        SendRequestStatus result = status.toSend();

        assertThat(result.currentSendRequestStatus())
                .isEqualTo(SendRequestStatusEnum.SENDING);
    }

    @Test
    @DisplayName("SCHEDULED 상태에서 COMPLETED 상태로 변경하면 에러가 발생한다.")
    void shouldThrowException_whenToCompleted() {
        SendRequestStatus status = new SchedulingStatus();

        SendCompletedStatusException result = SendCompletedStatusException.of(status.currentSendRequestStatus().code());

        assertThatThrownBy(() -> status.toComplete())
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("SCHEDULED 상태에서 ERROR 상태로 변경하면 ERROR 반환돈다.")
    void shouldThrowException_whenToError() {
        SendRequestStatus status = new SchedulingStatus();

        SendRequestStatus result = status.toError();

        assertThat(result.currentSendRequestStatus())
                .isEqualTo(SendRequestStatusEnum.FAILED);
    }
}