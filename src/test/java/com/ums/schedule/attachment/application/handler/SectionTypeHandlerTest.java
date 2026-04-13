package com.ums.schedule.attachment.application.handler;

import com.ums.schedule.application.channel.email.converter.handler.EmailBodyConvertHandler;
import com.ums.schedule.application.channel.email.converter.handler.PdfConvertHandler;
import com.ums.schedule.domain.channel.email.attachment.Attachment;
import com.ums.schedule.attachment.fixture.AttachmentFixture;
import com.ums.schedule.attachment.fixture.builder.EmailContentResponseBuilder;
import com.ums.schedule.attachment.fixture.builder.EmailMessageCommandBuilder;
import com.ums.schedule.adapter.api.send.email.EmailSendCreateRequest;
import com.ums.schedule.adapter.api.template.email.EmailContentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.ums.schedule.code.EnumMapperValue.fromEnumMapperType;
import static com.ums.schedule.domain.template.domain.code.ConvertTypeEnum.*;
import static com.ums.schedule.domain.template.domain.code.ConvertTypeEnum.HTML;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SectionTypeHandlerTest {
    @Mock private EmailBodyConvertHandler nextHandler;
    @InjectMocks private PdfConvertHandler handler;

    @Test
    @DisplayName("EmailContent의 Section이 BODY이면 다음 핸들러를 실행한다.")
    void shouldExecuteNextHandler_whenEmailContentSectionIsBody() {
        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
        EmailContentResponse body = AttachmentFixture.bodyOfHtml();
        when(nextHandler.handle(any(), any())).thenReturn(Attachment.of(fromEnumMapperType(NONE)));

        handler.handle(command, body);
        verify(nextHandler).handle(command, body);
    }

    @Test
    @DisplayName("EmailContent의 Section이 BODY이고 ConvertType이 NONE이면 NULL을 반환한다.")
    void shouldReturnNull_whenEmailContentSectionIsBody() {
        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
        EmailContentResponse body = AttachmentFixture.bodyOfHtml();

        when(nextHandler.handle(any(), any())).thenReturn(Attachment.of(fromEnumMapperType(NONE)));

        Attachment result = handler.handle(command, body);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("EmailContent의 Section이 BODY이고 convertType은 NONE이 아니면, .")
    void shouldReturnConvertTypeIsNotNone_whenEmailContentSectionIsBody() {
        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
        EmailContentResponse body = AttachmentFixture.bodyOfHtml();

        when(nextHandler.handle(any(), any())).thenReturn(Attachment.of(fromEnumMapperType(HTML)));

        Attachment result = handler.handle(command, body);
        assertThat(result.getConvertType()).isEqualTo(HTML);
    }

    @Test
    @DisplayName("EmailContent의 Section이 Attachment이면 ConvertType이 NONE인 Attachment를 반환한다.")
    void shouldReturnAttachmentConvertTypeNONE_whenEmailContentSectionIsAttachment() {
        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
        EmailContentResponse body = EmailContentResponseBuilder.builder()
                .section(EmailTemplateSectionEnum.ATTACHMENT)
                .build();

        Attachment result = handler.handle(command, body);

        assertThat(result.getConvertType()).isEqualTo(NONE);
    }

}