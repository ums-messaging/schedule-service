package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.domain.request.status.exception.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.ums.schedule.send.code.SendRequestStatusEnum.READY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestCreateStatusTest {

    @Test
    @DisplayName("CREATE 상태에서 READY 상태로 변경하면 READY가 반환된다.")
    void shouldReturnStatusIsReady_whenToReady() {
        SendRequestStatus createStatus = new RequestCreateStatus();

        SendRequestStatus result = createStatus.toReady();

        assertThat(result.currentSendRequestStatus()).isEqualTo(READY);
    }

    @Test
    @DisplayName("CREATE 상태에서 REQUEST 상태로 변경하면 에러가 발생한다.")
    void shouldThrowException_whenToRequest() {
        SendRequestStatus createStatus = new RequestCreateStatus();

        SendRequestStatusException result = SendRequestStatusException.of(createStatus.currentSendRequestStatus().code());

        assertThatThrownBy(() -> createStatus.toRequest())
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("CREATE 상태에서 SCHEDULE 상태로 변경하면 에러가 발생한다.")
    void shouldThrowException_whenToScheduled() {
        SendRequestStatus createStatus = new RequestCreateStatus();

        SchedulingStatusException result = SchedulingStatusException.of(createStatus.currentSendRequestStatus().code());

        assertThatThrownBy(() -> createStatus.toScheduled())
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("CREATE 상태에서 SEND 상태로 변경하면 에러가 발생한다.")
    void shouldThrowException_whenToSend() {
        SendRequestStatus createStatus = new RequestCreateStatus();

        SendingStatusException result = SendingStatusException.of(createStatus.currentSendRequestStatus().code());

        assertThatThrownBy(() -> createStatus.toSend())
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("CREATE 상태에서 Complete 상태로 변경하면 에러가 발생한다.")
    void shouldThrowException_whenToCompleted() {
        SendRequestStatus createStatus = new RequestCreateStatus();

        SendCompletedStatusException result = SendCompletedStatusException.of(createStatus.currentSendRequestStatus().code());

        assertThatThrownBy(() -> createStatus.toComplete())
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("CREATE 상태에서 Error 상태로 변경하면 Error가 반환돈다.")
    void shouldThrowException_whenToError() {
        SendRequestStatus createStatus = new RequestCreateStatus();

        SendRequestStatus result = createStatus.toError();

        assertThat(result.currentSendRequestStatus())
                .isEqualTo(SendRequestStatusEnum.FAILED);
    }
}