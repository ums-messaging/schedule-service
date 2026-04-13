package com.ums.schedule.send.domain.event;

import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.domain.request.event.SendRequestEvent;
import com.ums.schedule.message.domain.EmailSendMessage;
import com.ums.schedule.application.request.dto.SendRequestDto;
import com.ums.schedule.domain.request.CustomerRequestKey;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.status.RequestState;
import com.ums.schedule.domain.request.exception.status.SendStatusException;
import com.ums.schedule.domain.request.exception.status.SendingStatusException;
import com.ums.schedule.send.domain.target.upload.TargetUpload;
import com.ums.schedule.send.domain.target.upload.status.TargetCompletedStatus;
import com.ums.schedule.send.domain.target.upload.status.TargetUploadStatus;
import com.ums.schedule.domain.channel.email.message.EmailTitle;
import com.ums.schedule.domain.channel.email.message.EmailTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.ums.schedule.code.EnumMapperValue.fromEnumMapperType;

import static com.ums.schedule.domain.send.code.TargetUploadTypeEnum.FILE;
import static com.ums.schedule.domain.template.domain.code.TemplateContentFormatEnum.TEXT;
import static com.ums.schedule.domain.template.domain.code.TemplateTypeEnum.ADVERTISE;
import static org.assertj.core.api.Assertions.assertThat;

class SendRequestEventTest {

    private SendRequest request;

    @BeforeEach
    void setUp() {
        this.request = givenSendRequest();
    }

    @Test
    @DisplayName("이벤트 타입에 따라 전체 발송 흐름이 정상적으로 진행된다.")
    void shouldFollowFullFlow_whenEventType() {
        SendRequest request = givenSendRequest();
        givenEvent(request, fromEnumMapperType(JOB_CREATED)).getStatus();
        givenMessageCreatedEvent(request, fromEnumMapperType(MESSAGE_CREATED)).getStatus();
        givenTargetUpload(request, fromEnumMapperType(TARGET_UPLOADED)).getStatus();
        givenEvent(request, fromEnumMapperType(SEND_REQUEST)).getStatus();
        givenEvent(request, fromEnumMapperType(SCHEDULING)).getStatus();
        givenEvent(request, fromEnumMapperType(SEND_START)).getStatus();
        givenEvent(request, fromEnumMapperType(MESSAGE_MAKING)).getStatus();
        givenEvent(request, fromEnumMapperType(SENDING)).getStatus();
        givenEvent(request, fromEnumMapperType(SEND_END)).getStatus();

        assertThat(request.getStatus()).isEqualTo(SendRequestStatusEnum.COMPLETED);
    }

    @Test
    @DisplayName("SendRequestStatusException 발생 시 ResultCode는 Fail, ResultMessage는 오류 메시지를 반환한다.")
    void shouldReturnResultCodeIsFailAndMessage_whenThrowSendRequestStatusException() {
        SendRequest request = givenSendRequest();
        SendRequestEvent.of(request, fromEnumMapperType(JOB_CREATED));

        SendRequestEvent result = SendRequestEvent.of(request, fromEnumMapperType(SEND_START));
        SendStatusException exception = SendingStatusException.of(SendRequestStatusEnum.CREATE.code());
        assertThat(result.getResultCode())
                .isEqualTo(ResultCodeEnum.FAIL);
        assertThat(result.getResultMessage()).isEqualTo(exception.getMessage());
    }

    @Test
    @DisplayName("이벤트에 대한 상태 처리가 정상적으로 되면 결과 코드는 SUCCESS를 반환한다.")
    void shouldReturnResultCodeIsSuccess_whenChangeOnEvent() {
        SendRequestEvent event = SendRequestEvent.of(givenSendRequest(), fromEnumMapperType(SCHEDULING));
        event.mark(new RequestState());

        assertThat(event.getResultCode()).isEqualTo(ResultCodeEnum.SUCCESS);
        assertThat(event.getResultMessage()).isEqualTo(ResultCodeEnum.SUCCESS.description());
    }

    private SendRequest givenMessageCreatedEvent(SendRequest request, EnumMapperValue eventType) {
        request.(givenSendMessage());
        givenEvent(request, eventType);
        return request;
    }
    private SendRequest givenTargetUpload(SendRequest request, EnumMapperValue eventType) {
        TargetUpload targetUpload = TargetUpload.of(fromEnumMapperType(FILE));
        targetUpload.changeStatus(new TargetCompletedStatus());
        targetUpload.applySendRequest(request);
        givenEvent(request, eventType);
        return request;
    }

    private SendRequest givenEvent(SendRequest sendRequest, EnumMapperValue fromEnumMapperType) {
        return SendRequestEvent.of(sendRequest, fromEnumMapperType).getSendRequest();
    }


    private SendRequest givenSendRequest() {
        CustomerRequestKey customerKey = CustomerRequestKey.of("company", UUID.randomUUID().toString());
        SendRequestDto dto = new SendRequestDto(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                3,
                1000
        );

        SendRequest request = SendRequest.of(customerKey, dto);
        return request;
    }

    private EmailSendMessage givenSendMessage() {
        EmailTitle content = EmailTitle.ofWithoutPrefix(fromEnumMapperType(ADVERTISE), "광고");
        EmailContentDto dto = new EmailContentDto(
                EmailContent.of(fromEnumMapperType(TEXT), "header"),
                EmailContent.of(fromEnumMapperType(TEXT), "body"),
                EmailContent.of(fromEnumMapperType(TEXT), "footer")
        );
        EmailTemplate template = EmailTemplate.of(UUID.randomUUID().toString(), dto);
        EmailSendMessage message = EmailSendMessage.of(content, template, List.of());
        message.toComplete();
        return message;
    }

}