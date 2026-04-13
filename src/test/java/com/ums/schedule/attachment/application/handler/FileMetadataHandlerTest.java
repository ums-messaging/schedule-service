package com.ums.schedule.attachment.application.handler;

import com.ums.schedule.application.channel.email.converter.handler.EmailBodyConvertHandler;
import com.ums.schedule.application.channel.email.converter.handler.HtmlUploadHandler;
import com.ums.schedule.domain.channel.email.attachment.Attachment;
import com.ums.schedule.domain.channel.email.exception.AttachmentPolicyRequiredException;
import com.ums.schedule.attachment.fixture.builder.EmailContentResponseBuilder;
import com.ums.schedule.attachment.fixture.builder.EmailMessageCommandBuilder;
import com.ums.schedule.code.EnumMapperFactory;
import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.adapter.api.send.email.EmailSendCreateRequest;
import com.ums.schedule.adapter.api.template.email.EmailContentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.ums.schedule.domain.template.domain.code.ConvertTypeEnum.PDF;
import static com.ums.schedule.domain.template.domain.code.EmailTemplateSectionEnum.ATTACHMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileMetadataHandlerTest {
    @Mock private EnumMapperFactory factory;
    @Mock private EmailBodyConvertHandler nextHandler;
    @InjectMocks
    HtmlUploadHandler handler;

    @Test
    @DisplayName("ContentType이 존재하지 않으면 예외가 발생한다.")
    void shouldThrowException_whenContentTypeDoesNotExist() {
        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
        EmailContentResponse content = EmailContentResponseBuilder
                .builder()
                .section(ATTACHMENT)
                .contentType(null)
                .build();

        Attachment attachment = Attachment.of(EnumMapperValue.fromEnumMapperType(PDF));

        when(nextHandler.handle(any(), any())).thenReturn(attachment);
        when(factory.findEnumMapperValue(any(), any()))
                .thenReturn(EnumMapperValue.fromEnumMapperType(StorageTypeEnum.S3));

        assertThatThrownBy(() -> handler.handle(command, content))
                .isInstanceOf(AttachmentPolicyRequiredException.class)
                .hasMessage(AttachmentPolicyRequiredException.ofFileMetadata("Content type ").getMessage());
    }

    @Test
    @DisplayName("fileSize가 존재하지 않으면 예외가 발생한다.")
    void shouldThrowException_whenFileSizeDoesNotExist() {
        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
        EmailContentResponse content = EmailContentResponseBuilder
                .builder()
                .section(ATTACHMENT)
                .fileSize(null)
                .build();

        Attachment attachment = Attachment.of(EnumMapperValue.fromEnumMapperType(PDF));

        when(nextHandler.handle(any(), any())).thenReturn(attachment);
        when(factory.findEnumMapperValue(any(), any()))
                .thenReturn(EnumMapperValue.fromEnumMapperType(StorageTypeEnum.S3));

        assertThatThrownBy(() -> handler.handle(command, content))
                .isInstanceOf(AttachmentPolicyRequiredException.class)
                .hasMessage(AttachmentPolicyRequiredException.ofFileMetadata("File size ").getMessage());
    }

    @Test
    @DisplayName("fileKey가 존재하지 않으면 예외가 발생한다.")
    void shouldThrowException_whenFileKeyDoesNotExist() {
        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
        EmailContentResponse content = EmailContentResponseBuilder
                .builder()
                .section(ATTACHMENT)
                .fileKey(null)
                .build();

        Attachment attachment = Attachment.of(EnumMapperValue.fromEnumMapperType(PDF));

        when(nextHandler.handle(any(), any())).thenReturn(attachment);
        when(factory.findEnumMapperValue(any(), any()))
                .thenReturn(EnumMapperValue.fromEnumMapperType(StorageTypeEnum.S3));

        assertThatThrownBy(() -> handler.handle(command, content))
                .isInstanceOf(AttachmentPolicyRequiredException.class)
                .hasMessage(AttachmentPolicyRequiredException.ofFileMetadata("File key ").getMessage());
    }
    @Test
    @DisplayName("originalFileName이 존재하지 않으면 예외가 발생한다.")
    void shouldThrowException_whenOriginalFileNameDoesNotExist() {
        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
        EmailContentResponse content = EmailContentResponseBuilder
                .builder()
                .section(ATTACHMENT)
                .originalFileName(null)
                .build();

        Attachment attachment = Attachment.of(EnumMapperValue.fromEnumMapperType(PDF));

        when(nextHandler.handle(any(), any())).thenReturn(attachment);
        when(factory.findEnumMapperValue(any(), any()))
                .thenReturn(EnumMapperValue.fromEnumMapperType(StorageTypeEnum.S3));

        assertThatThrownBy(() -> handler.handle(command, content))
                .isInstanceOf(AttachmentPolicyRequiredException.class)
                .hasMessage(AttachmentPolicyRequiredException.ofFileMetadata("Original file name ").getMessage());
    }

    @Test
    @DisplayName("Attachment의 FileMetaData는 NULL이 아니다.")
    void shouldReturnFileMetaDataIsNotNull() {
        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
        EmailContentResponse content = EmailContentResponseBuilder
                .builder()
                .section(ATTACHMENT)
                .build();

        Attachment attachment = Attachment.of(EnumMapperValue.fromEnumMapperType(PDF));

        when(nextHandler.handle(any(), any())).thenReturn(attachment);
        when(factory.findEnumMapperValue(any(), any()))
                .thenReturn(EnumMapperValue.fromEnumMapperType(StorageTypeEnum.S3));

        Attachment result = handler.handle(command, content);

        assertThat(result.getFileMetaData()).isNotNull();
    }
}