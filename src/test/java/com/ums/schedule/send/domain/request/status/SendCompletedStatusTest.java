package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.send.application.model.dto.SendRequestDto;
import com.ums.schedule.send.domain.event.SendRequestEvent;
import com.ums.schedule.send.domain.request.CustomerRequestKey;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.request.status.exception.*;
import com.ums.schedule.target.domain.status.SendTargetCreatedStatus;
import com.ums.schedule.target.domain.status.SendTargetReadyStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.ums.schedule.common.code.EnumMapperValue.fromEnumMapperType;
import static com.ums.schedule.send.code.SendRequestEventEnum.*;
import static com.ums.schedule.send.code.SendRequestEventEnum.SEND_END;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SendCompletedStatusTest {

    @Test
    @DisplayName("JOB_CREATED 이벤트를 전달하면 에러가 발생한다. ")
    void shouldThrowException_whenEventOnJobCreated() {
        SendRequestState state = new SendReadyState();
        SendRequest request = givenSendRequest();
        SendRequestEvent event = SendRequestEvent.of(request, fromEnumMapperType(JOB_CREATED));

        // when
        SendRequestState result = state.onEvent(event);

        // then
        assertThat(result).isInstanceOf(SendTargetCreatedStatus.class);
    }

    @Test
    @DisplayName("MESSAGE_CREATED 이벤트를 전달하면 READY를 반환한다. ")
    void shouldThrowException_whenEventOnMessageCreated() {
        SendRequestState state = new SendReadyState();
        SendRequest request = givenSendRequest();

        SendRequestEvent event = SendRequestEvent.of(request, fromEnumMapperType(MESSAGE_CREATED));

        // when
        SendRequestState result = state.onEvent(event);

        // then
        assertThat(result).isInstanceOf(SendTargetReadyStatus.class);
    }

    @DisplayName("TARGET_UPLOADED 이벤트를 전달하면 READY를 반환한다.")
    void shouldReturnStatusIsReady_whenEventOnTargetUploaded() {
        SendRequestState state = new SendReadyState();
        SendRequest request = givenSendRequest();

        SendRequestEvent event = SendRequestEvent.of(request, fromEnumMapperType(MESSAGE_CREATED));

        // when
        SendRequestState result = state.onEvent(event);

        // then
        assertThat(result).isInstanceOf(SendTargetReadyStatus.class);
    }

    @Test
    @DisplayName("SEND_REQUEST 이벤트를 전달하면 REQUEST를 반환한다.")
    void shouldReturnStatusIsRequest_whenEventOnSendRequest() {
        SendRequestState state = new SendReadyState();
        SendRequest request = givenSendRequest();

        SendRequestEvent event = SendRequestEvent.of(request, fromEnumMapperType(SEND_REQUEST));

        SendRequestStatusException result = SendRequestStatusException.of(state.currentSendRequestStatus().code());

        // when
        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("SCHEDULING 이벤트를 전달하면, 에러가 발생한다.")
    void shouldThrowException_whenToScheduled() {
        SendRequestState state = new SendReadyState();
        SendRequestEvent event = SendRequestEvent.of(givenSendRequest(), fromEnumMapperType(SCHEDULING));

        SchedulingStatusException result = SchedulingStatusException.of(state.currentSendRequestStatus().code());

        // when
        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("SEND_START 이벤트를 전달하면, 에러가 발생한다.")
    void shouldThrowException_whenToSendStart() {
        SendRequestState state = new SendReadyState();

        SendRequestEvent event = SendRequestEvent.of(givenSendRequest(), fromEnumMapperType(SEND_START));
        SendingStatusException result = SendingStatusException.of(state.currentSendRequestStatus().code());

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("SENDING 이벤트를 전달하면, 에러가 발생한다.")
    void shouldThrowException_whenToSending() {
        SendRequestState state = new SendReadyState();

        SendRequestEvent event = SendRequestEvent.of(givenSendRequest(), fromEnumMapperType(SENDING));
        SendingStatusException result = SendingStatusException.of(state.currentSendRequestStatus().code());

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("MESSAGE_MAKING 이벤트를 전달하면, 에러가 발생한다.")
    void shouldThrowException_whenToSendEnd() {
        SendRequestState state = new SendReadyState();

        SendRequestEvent event = SendRequestEvent.of(givenSendRequest(), fromEnumMapperType(MESSAGE_MAKING));
        SendingStatusException result = SendingStatusException.of(state.currentSendRequestStatus().code());

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("SEND_END 이벤트를 전달하면 에러가 발생한다.")
    void shouldThrowException_whenToCompleted() {
        SendRequestState state = new SendReadyState();
        SendRequestEvent event = SendRequestEvent.of(givenSendRequest(), fromEnumMapperType(SEND_END));

        SendCompletedStatusException result = SendCompletedStatusException.of(state.currentSendRequestStatus().code());

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    private SendRequest givenSendRequest() {
        CustomerRequestKey customerKey = CustomerRequestKey.of("company", UUID.randomUUID().toString());
        SendRequestDto dto = new SendRequestDto(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                3,
                1000
        );
        return SendRequest.of(customerKey, dto);
    }
}