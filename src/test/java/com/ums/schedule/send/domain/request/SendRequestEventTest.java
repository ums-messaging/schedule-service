package com.ums.schedule.send.domain.request;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.domain.email.EmailSendMessage;
import com.ums.schedule.send.application.model.dto.SendRequestDto;
import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.domain.target.upload.TargetUpload;
import com.ums.schedule.template.application.dto.EmailContentDto;
import com.ums.schedule.template.domain.TemplateTypeContent;
import com.ums.schedule.template.domain.code.TemplateContentFormatEnum;
import com.ums.schedule.template.domain.code.TemplateTypeEnum;
import com.ums.schedule.template.domain.email.EmailContent;
import com.ums.schedule.template.domain.email.EmailTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.ums.schedule.common.code.EnumMapperValue.fromEnumMapperType;
import static com.ums.schedule.send.code.TargetUploadTypeEnum.FILE;
import static com.ums.schedule.send.domain.target.upload.TargetUpload.of;
import static com.ums.schedule.template.domain.code.TemplateContentFormatEnum.TEXT;
import static com.ums.schedule.template.domain.code.TemplateTypeEnum.ADVERTISE;
import static org.assertj.core.api.Assertions.assertThat;

class SendRequestEventTest {

    @Test
    @DisplayName("SendRequestEvent를 생성하면 status는 CREATE와 CreateAt이 반환된다.")
    void shouldReturnStatusIsCreatedAndCreatedAtIsNotNull_CreateSendRequestEvent() {
        SendRequestEvent event = new SendRequestEvent();

        assertThat(event.getStatus())
                .isEqualTo(SendRequestStatusEnum.CREATE);
    }

    @Test
    @DisplayName("대상자 처리가 완료되지 않은 상태에서 markMessageCreated를 호출하면 Status는 CREATE이다.")
    void shouldReturnStatusIsCreated_whenMarkMessageCreatedAndSendUploadStatusIs() {
        SendRequestEvent event = new SendRequestEvent();
        TargetUpload targetUpload = of(givenSendRequest(), fromEnumMapperType(FILE));

        event.markMessageCreated(targetUpload.getSendRequest());

        assertThat(event.getStatus()).isEqualTo(SendRequestStatusEnum.CREATE);
        assertThat(event.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("메시지 처리가 되지 않은 상태에서 markTargetUpload를 호출하면 Status는 CREATE를 반환한다.")
    void shouldReturnStatusIsCreated_whenMarkTargetUpload() {
        SendRequestEvent event = new SendRequestEvent();
        TargetUpload targetUpload = of(givenSendRequest(), fromEnumMapperType(FILE));
        targetUpload.completed();

        SendRequest sendRequest = targetUpload.getSendRequest();

        event.markTargetUpload(sendRequest);

        assertThat(event.getStatus()).isEqualTo(SendRequestStatusEnum.CREATE);
    }

    @Test
    @DisplayName("메시지 처리가 된 상태에서 markTargetUpload를 호출하면 Status는 READY를 반환한다.")
    void shouldReturnStatusIsReady_whenMarkTargetUpload() {
        SendRequestEvent event = new SendRequestEvent();
        TargetUpload targetUpload = of(givenSendRequest(), fromEnumMapperType(FILE));
        targetUpload.completed();

        SendRequest sendRequest = targetUpload.getSendRequest();
        sendRequest.applySendMessage(givenSendMessage());

        event.markTargetUpload(sendRequest);

        assertThat(event.getStatus()).isEqualTo(SendRequestStatusEnum.READY);
    }

    @Test
    @DisplayName("READY 상태에서 markRequest()를 호출 하면 REQUEST가 반환된다.")
    void shouldReturnStatusIsRequest_whenCallMarkRequest() {
        SendRequestEvent event = new SendRequestEvent();
        TargetUpload targetUpload = of(givenSendRequest(), fromEnumMapperType(FILE));
        targetUpload.completed();

        SendRequest sendRequest = targetUpload.getSendRequest();
        sendRequest.applySendMessage(givenSendMessage());
        event.markTargetUpload(sendRequest);
        event.markRequest();

        assertThat(event.getStatus()).isEqualTo(SendRequestStatusEnum.REQUEST);
        assertThat(event.getRequestedAt()).isNotNull();
    }

    @Test
    @DisplayName("REQUEST 상태에서 markScheduled()를 호출 하면 SCHEDULING이 반환된다.")
    void shouldReturnStatusIsScheduling_whenCallMarkScheduling() {
        SendRequestEvent event = new SendRequestEvent();
        TargetUpload targetUpload = of(givenSendRequest(), fromEnumMapperType(FILE));
        targetUpload.completed();

        SendRequest sendRequest = targetUpload.getSendRequest();
        sendRequest.applySendMessage(givenSendMessage());
        event.markTargetUpload(sendRequest);
        event.markRequest();
        event.markScheduled();

        assertThat(event.getStatus()).isEqualTo(SendRequestStatusEnum.SCHEDULING);
        assertThat(event.getScheduledAt()).isNotNull();
    }

    @Test
    @DisplayName("SCHEDULE 상태에서 markSend()를 호출 하면 SENDING을 반환된다.")
    void shouldReturnStatusIsSending_whenCallMarkSending() {
        SendRequestEvent event = new SendRequestEvent();
        TargetUpload targetUpload = of(givenSendRequest(), fromEnumMapperType(FILE));
        targetUpload.completed();

        SendRequest sendRequest = targetUpload.getSendRequest();
        sendRequest.applySendMessage(givenSendMessage());
        event.markTargetUpload(sendRequest);
        event.markRequest();
        event.markScheduled();
        event.markSend();

        assertThat(event.getStatus()).isEqualTo(SendRequestStatusEnum.SENDING);
        assertThat(event.getSendStartedAt()).isNotNull();
    }

    @Test
    @DisplayName("SENDING 상태에서 markCompleted()를 호출 하면 COMPLETED 반환된다.")
    void shouldReturnStatusIsCompleted_whenCallMarkCompleted() {
        SendRequestEvent event = new SendRequestEvent();
        TargetUpload targetUpload = of(givenSendRequest(), fromEnumMapperType(FILE));
        targetUpload.completed();

        SendRequest sendRequest = targetUpload.getSendRequest();
        sendRequest.applySendMessage(givenSendMessage());
        event.markTargetUpload(sendRequest);
        event.markRequest();
        event.markScheduled();
        event.markSend();
        event.markCompleted();

        assertThat(event.getStatus()).isEqualTo(SendRequestStatusEnum.SUCCESS);
        assertThat(event.getSendEndedAt()).isNotNull();
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
}