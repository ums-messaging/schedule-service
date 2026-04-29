package com.ums.schedule.attachment.application.handler;

import com.ums.schedule.application.channel.email.converter.handler.EmailBodyConvertHandler;
import com.ums.schedule.application.channel.email.converter.handler.HtmlUploadHandler;
import com.ums.schedule.code.EnumMapperFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class FileMetadataHandlerTest {
    @Mock private EnumMapperFactory factory;
    @Mock private EmailBodyConvertHandler nextHandler;
    @InjectMocks
    HtmlUploadHandler handler;

//    @Test
//    @DisplayName("ContentType이 존재하지 않으면 예외가 발생한다.")
//    void shouldThrowException_whenContentTypeDoesNotExist() {
//        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
//        EmailContentResponse content = EmailContentResponseBuilder
//                .builder()
//                .section(ATTACHMENT)
//                .contentType(null)
//                .build();
//
//        Attachment attachment = Attachment.of(EnumMapperValue.fromEnumMapperType(PDF));
//
//        when(nextHandler.handle(any(), any())).thenReturn(attachment);
//        when(factory.findEnumMapperValue(any(), any()))
//                .thenReturn(EnumMapperValue.fromEnumMapperType(StorageTypeEnum.S3));
//
//        assertThatThrownBy(() -> handler.handle(command, content))
//                .isInstanceOf(AttachmentPolicyRequiredException.class)
//                .hasMessage(AttachmentPolicyRequiredException.ofFileMetadata("Content type ").getMessage());
//    }
//
//    @Test
//    @DisplayName("fileSize가 존재하지 않으면 예외가 발생한다.")
//    void shouldThrowException_whenFileSizeDoesNotExist() {
//        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
//        EmailContentResponse content = EmailContentResponseBuilder
//                .builder()
//                .section(ATTACHMENT)
//                .fileSize(null)
//                .build();
//
//        Attachment attachment = Attachment.of(EnumMapperValue.fromEnumMapperType(PDF));
//
//        when(nextHandler.handle(any(), any())).thenReturn(attachment);
//        when(factory.findEnumMapperValue(any(), any()))
//                .thenReturn(EnumMapperValue.fromEnumMapperType(StorageTypeEnum.S3));
//
//        assertThatThrownBy(() -> handler.handle(command, content))
//                .isInstanceOf(AttachmentPolicyRequiredException.class)
//                .hasMessage(AttachmentPolicyRequiredException.ofFileMetadata("File size ").getMessage());
//    }
//
//    @Test
//    @DisplayName("fileKey가 존재하지 않으면 예외가 발생한다.")
//    void shouldThrowException_whenFileKeyDoesNotExist() {
//        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
//        EmailContentResponse content = EmailContentResponseBuilder
//                .builder()
//                .section(ATTACHMENT)
//                .fileKey(null)
//                .build();
//
//        Attachment attachment = Attachment.of(EnumMapperValue.fromEnumMapperType(PDF));
//
//        when(nextHandler.handle(any(), any())).thenReturn(attachment);
//        when(factory.findEnumMapperValue(any(), any()))
//                .thenReturn(EnumMapperValue.fromEnumMapperType(StorageTypeEnum.S3));
//
//        assertThatThrownBy(() -> handler.handle(command, content))
//                .isInstanceOf(AttachmentPolicyRequiredException.class)
//                .hasMessage(AttachmentPolicyRequiredException.ofFileMetadata("File key ").getMessage());
//    }
//    @Test
//    @DisplayName("originalFileName이 존재하지 않으면 예외가 발생한다.")
//    void shouldThrowException_whenOriginalFileNameDoesNotExist() {
//        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
//        EmailContentResponse content = EmailContentResponseBuilder
//                .builder()
//                .section(ATTACHMENT)
//                .originalFileName(null)
//                .build();
//
//        Attachment attachment = Attachment.of(EnumMapperValue.fromEnumMapperType(PDF));
//
//        when(nextHandler.handle(any(), any())).thenReturn(attachment);
//        when(factory.findEnumMapperValue(any(), any()))
//                .thenReturn(EnumMapperValue.fromEnumMapperType(StorageTypeEnum.S3));
//
//        assertThatThrownBy(() -> handler.handle(command, content))
//                .isInstanceOf(AttachmentPolicyRequiredException.class)
//                .hasMessage(AttachmentPolicyRequiredException.ofFileMetadata("Original file name ").getMessage());
//    }
//
//    @Test
//    @DisplayName("Attachment의 FileMetaData는 NULL이 아니다.")
//    void shouldReturnFileMetaDataIsNotNull() {
//        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
//        EmailContentResponse content = EmailContentResponseBuilder
//                .builder()
//                .section(ATTACHMENT)
//                .build();
//
//        Attachment attachment = Attachment.of(EnumMapperValue.fromEnumMapperType(PDF));
//
//        when(nextHandler.handle(any(), any())).thenReturn(attachment);
//        when(factory.findEnumMapperValue(any(), any()))
//                .thenReturn(EnumMapperValue.fromEnumMapperType(StorageTypeEnum.S3));
//
//        Attachment result = handler.handle(command, content);
//
//        assertThat(result.getFileMetaData()).isNotNull();
//    }
}