package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.common.code.EnumMapperType;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.domain.email.EmailSendMessage;
import com.ums.schedule.send.application.model.dto.SendRequestDto;
import com.ums.schedule.send.code.SendRequestEventEnum;
import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.domain.event.SendRequestEvent;
import com.ums.schedule.send.domain.request.CustomerRequestKey;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.request.status.exception.*;
import com.ums.schedule.send.domain.target.status.SendTargetCreatedStatus;
import com.ums.schedule.send.domain.target.status.SendTargetReadyStatus;
import com.ums.schedule.send.domain.target.upload.TargetUpload;
import com.ums.schedule.template.application.dto.EmailContentDto;
import com.ums.schedule.template.domain.TemplateTypeContent;
import com.ums.schedule.template.domain.email.EmailContent;
import com.ums.schedule.template.domain.email.EmailTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.ums.schedule.common.code.EnumMapperValue.fromEnumMapperType;
import static com.ums.schedule.send.code.SendRequestEventEnum.*;
import static com.ums.schedule.send.code.SendRequestStatusEnum.READY;
import static com.ums.schedule.send.code.TargetUploadTypeEnum.FILE;
import static com.ums.schedule.template.domain.code.TemplateContentFormatEnum.TEXT;
import static com.ums.schedule.template.domain.code.TemplateTypeEnum.ADVERTISE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestCreateStatusTest {

    @Test
    @DisplayName("대상자 처리 상태에서 MESSAGE_CREATED 이벤트를 전달하면, READY를 반환한다.")
    void shouldReturnStatusIsReady_whenEventOnMessageCreatedAndTargetUploadIsSuccess() {
        RequestCreateState state = new RequestCreateState();
        SendRequest request = givenSendRequest();
        request.applySendMessage(givenSendMessage());
        givenTargetUpload(request);

        SendRequestEvent event = SendRequestEvent.of(request, fromEnumMapperType(MESSAGE_CREATED));

        // when
        SendRequestState result = state.onEvent(event);

        // then
        assertThat(result).isInstanceOf(SendTargetReadyStatus.class);
    }

    @Test
    @DisplayName("메시지 처리 상태에서 TARGET_UPLOAD 이벤트를 전달하면, READY를 반환한다.")
    void shouldReturnStatusIsReady_whenEventOnUploadingTargetAndCreatingMessage() {
        RequestCreateState state = new RequestCreateState();
        SendRequest request = givenSendRequest();
        request.applySendMessage(givenSendMessage());
        givenTargetUpload(request);

        SendRequestEvent event = SendRequestEvent.of(request, fromEnumMapperType(TARGET_UPLOADED));

        // when
        SendRequestState result = state.onEvent(event);

        // then
        assertThat(result).isInstanceOf(SendTargetReadyStatus.class);
    }

    @Test
    @DisplayName("대상자가 처리되지 않은 상태에서 MESSAGE_CREATED 이벤트를 전달하면, CREATE를 반환한다.")
    void shouldReturnStatusIsReady_whenEventOnCreatingMessageAndNotUploadingTarget() {
        RequestCreateState state = new RequestCreateState();
        SendRequest request = givenSendRequest();
        request.applySendMessage(givenSendMessage());

        SendRequestEvent event = SendRequestEvent.of(request, fromEnumMapperType(TARGET_UPLOADED));

        // when
        SendRequestState result = state.onEvent(event);

        // then
        assertThat(result).isInstanceOf(SendTargetCreatedStatus.class);
    }

    @Test
    @DisplayName("JOB_CREATED 이벤트를 전달하면 CREATED를 반환한다. ")
    void shouldReturnStatusIsCreated_whenEventOnJobCreated() {
        RequestCreateState state = new RequestCreateState();
        SendRequest request = givenSendRequest();
        SendRequestEvent event = SendRequestEvent.of(request, fromEnumMapperType(JOB_CREATED));

        // when
        SendRequestState result = state.onEvent(event);

        // then
        assertThat(result).isInstanceOf(SendTargetCreatedStatus.class);
    }

    @Test
    @DisplayName("SEND_REQUEST 이벤트를 전달하면, 에러가 발생한다.")
    void shouldThrowException_whenEventOnSendRequest() {
        RequestCreateState state = new RequestCreateState();
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
        RequestCreateState state = new RequestCreateState();
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
        SendRequestState state = new RequestCreateState();

        SendRequestEvent event = SendRequestEvent.of(givenSendRequest(), fromEnumMapperType(SEND_START));
        SendingStatusException result = SendingStatusException.of(state.currentSendRequestStatus().code());

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("SENDING 이벤트를 전달하면, 에러가 발생한다.")
    void shouldThrowException_whenToSending() {
        SendRequestState state = new RequestCreateState();

        SendRequestEvent event = SendRequestEvent.of(givenSendRequest(), fromEnumMapperType(SENDING));
        SendingStatusException result = SendingStatusException.of(state.currentSendRequestStatus().code());

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("MESSAGE_MAKING 이벤트를 전달하면, 에러가 발생한다.")
    void shouldThrowException_whenToSendEnd() {
        SendRequestState state = new RequestCreateState();

        SendRequestEvent event = SendRequestEvent.of(givenSendRequest(), fromEnumMapperType(MESSAGE_MAKING));
        SendingStatusException result = SendingStatusException.of(state.currentSendRequestStatus().code());

        assertThatThrownBy(() -> state.onEvent(event))
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("SEND_END 이벤트를 전달하면 에러가 발생한다.")
    void shouldThrowException_whenToCompleted() {
        SendRequestState state = new RequestCreateState();
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

    private EmailSendMessage givenSendMessage() {
        TemplateTypeContent content = TemplateTypeContent.ofWithoutPrefix(fromEnumMapperType(ADVERTISE), "광고");
        EmailContentDto dto = new EmailContentDto(
                EmailContent.of(fromEnumMapperType(TEXT), "header"),
                EmailContent.of(fromEnumMapperType(TEXT), "body"),
                EmailContent.of(fromEnumMapperType(TEXT), "footer")
        );
        EmailTemplate template = EmailTemplate.of(UUID.randomUUID().toString(), dto);
        return EmailSendMessage.of(content, template, List.of());
    }
    private TargetUpload givenTargetUpload(SendRequest request) {
        TargetUpload targetUpload = TargetUpload.of(fromEnumMapperType(FILE));
        targetUpload.applySendRequest(request);
        return targetUpload;
    }
}