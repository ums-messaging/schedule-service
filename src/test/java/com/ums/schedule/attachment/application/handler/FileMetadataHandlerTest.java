package com.ums.schedule.attachment.application.handler;

import com.ums.schedule.attachment.code.StorageTypeEnum;
import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.attachment.exception.AttachmentPolicyRequiredException;
import com.ums.schedule.attachment.fixture.builder.EmailContentResponseBuilder;
import com.ums.schedule.attachment.fixture.builder.EmailMessageCommandBuilder;
import com.ums.schedule.common.code.EnumMapperFactory;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.application.command.EmailMessageCommand;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;
import com.ums.schedule.template.domain.code.EmailTemplateSectionEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.ums.schedule.template.domain.code.ConvertTypeEnum.PDF;
import static com.ums.schedule.template.domain.code.EmailTemplateSectionEnum.ATTACHMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileMetadataHandlerTest {
    @Mock private EnumMapperFactory factory;
    @Mock private AttachmentHandler nextHandler;
    @InjectMocks FileMetadataHandler handler;

    @Test
    @DisplayName("ContentType이 존재하지 않으면 예외가 발생한다.")
    void shouldThrowException_whenContentTypeDoesNotExist() {
        EmailMessageCommand command = EmailMessageCommandBuilder.builder().build();
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
        EmailMessageCommand command = EmailMessageCommandBuilder.builder().build();
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
        EmailMessageCommand command = EmailMessageCommandBuilder.builder().build();
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
        EmailMessageCommand command = EmailMessageCommandBuilder.builder().build();
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
        EmailMessageCommand command = EmailMessageCommandBuilder.builder().build();
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