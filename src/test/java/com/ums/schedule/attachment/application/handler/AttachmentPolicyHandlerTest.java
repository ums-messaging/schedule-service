package com.ums.schedule.attachment.application.handler;

import com.ums.schedule.application.channel.email.converter.handler.EmailBodyConvertHandler;
import com.ums.schedule.application.channel.email.converter.handler.PdfConvertHandler;
import com.ums.schedule.domain.channel.email.attachment.Attachment;
import com.ums.schedule.domain.channel.email.exception.AttachmentPolicyRequiredException;
import com.ums.schedule.attachment.fixture.AttachmentFixture;
import com.ums.schedule.attachment.fixture.builder.EmailContentResponseBuilder;
import com.ums.schedule.attachment.fixture.builder.EmailMessageCommandBuilder;
import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.adapter.api.send.email.EmailSendCreateRequest;
import com.ums.schedule.adapter.api.template.email.EmailContentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.ums.schedule.domain.template.domain.code.ConvertTypeEnum.*;
import static com.ums.schedule.domain.template.domain.code.ConvertTypeEnum.HTML;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttachmentPolicyHandlerTest {
    @Mock private EmailBodyConvertHandler nextHandler;
    @InjectMocks private PdfConvertHandler handler;

    @Test
    @DisplayName("첨부파일 입력 정보가 존재하지 않으면 예외가 발생한다. ")
    void shouldReturnNull_whenAttachmentPolicyNotExist() {
        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
        EmailContentResponse body = EmailContentResponseBuilder.builder()
                .attachmentName(null)
                .downloadName(null)
                .build();

        when(nextHandler.handle(any(), any())).thenReturn(Attachment.of(EnumMapperValue.fromEnumMapperType(NONE)));

        assertThatThrownBy(() -> handler.handle(command, body))
                .isInstanceOf(AttachmentPolicyRequiredException.class)
                .hasMessage(AttachmentPolicyRequiredException.ofDownloadOrAttachmentName().getMessage());
        verify(nextHandler).handle(command, body);

    }

    @Test
    @DisplayName("BODY Attachment가 NULL 이면, 첨부파일 리스트만 반환된다.")
    void shouldReturnAttachmentListOnly_whenBodyConvertTypeIsNone() {
        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();

        List<EmailContentResponse> attachmentList = AttachmentFixture.ofAttachmentList(3);
        when(nextHandler.handle(any(), any())).thenReturn(Attachment.of(EnumMapperValue.fromEnumMapperType(NONE)));

        List<Attachment> result = handler.handle(command, null, attachmentList);

        assertThat(result.size()).isEqualTo(attachmentList.size());
    }


    @Test
    @DisplayName("BODY Attachment가 NULL이 아니면 첨부파일 정보로 변환되어 첨부파일 목록과 함께 반환된다.")
    void shouldReturnAttachmentListWithBody_whenSecurityPolicyCommandDoesNotExist() {
        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
        Attachment body = Attachment.of(EnumMapperValue.fromEnumMapperType(HTML));

        List<EmailContentResponse> attachmentList = AttachmentFixture.ofAttachmentList(3);
        when(nextHandler.handle(any(), any())).thenReturn(Attachment.of(EnumMapperValue.fromEnumMapperType(NONE)));

        List<Attachment> result = handler.handle(command, body, attachmentList);
        assertThat(attachmentList.size() + 1).isEqualTo(result.size());
    }
}