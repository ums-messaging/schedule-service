package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.message.domain.EmailSendMessage;
import com.ums.schedule.application.request.dto.SendRequestDto;
import com.ums.schedule.domain.request.CustomerRequestKey;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.send.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.channel.email.message.EmailTitle;
import com.ums.schedule.domain.channel.email.message.EmailTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.ums.schedule.code.EnumMapperValue.fromEnumMapperType;
import static com.ums.schedule.domain.send.code.TargetUploadTypeEnum.FILE;
import static com.ums.schedule.domain.template.domain.code.TemplateContentFormatEnum.TEXT;
import static com.ums.schedule.domain.template.domain.code.TemplateTypeEnum.ADVERTISE;

class SendRequestStateTest {

    @Test
    @DisplayName("상태전이 총 테스트")
    void shouldReturnStatusIsCompleted_whenAllStatusProcess() {

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
        request.applySendMessage(givenSendMessage());
        TargetUpload targetUpload = TargetUpload.of(fromEnumMapperType(FILE));
        targetUpload.applySendRequest(request);
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
        return EmailSendMessage.of(content, template, List.of());
    }

}