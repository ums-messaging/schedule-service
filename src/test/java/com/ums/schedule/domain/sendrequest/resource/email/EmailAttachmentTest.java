package com.ums.schedule.domain.sendrequest.resource.email;

import com.ums.schedule.application.resource.email.command.EmailAttachmentCreateCommandBuilder;
import com.ums.schedule.application.message.email.model.AttachmentCreateCommand;
import com.ums.schedule.application.message.email.model.EmailMessageCreateCommandBuilder;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.exception.validation.InvalidFileExtensionException;
import com.ums.schedule.common.exception.validation.RequiredException;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;
import com.ums.schedule.domain.sendrequest.resource.email.code.EncryptionTypeEnum;
import com.ums.schedule.domain.sendrequest.resource.email.code.PasswordHashEnum;
import com.ums.schedule.domain.sendrequest.resource.email.code.PermissionMaskEnum;
import com.ums.schedule.domain.sendrequest.resource.email.policy.SecurityPolicy;
import com.ums.schedule.domain.sendrequest.message.email.EmailSendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class EmailAttachmentTest {


    @Nested
    @DisplayName("convert_type이 NONE일 때")
    class WhenConvertTypeIsNone {
        private EmailAttachmentCreateCommandBuilder builder;
        private EmailSendMessage sendMessage;

        @BeforeEach
        void setUp() {
            this.sendMessage = mock(EmailSendMessage.class);
            this.builder = EmailAttachmentCreateCommandBuilder.builder()
                    .convertType(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.NONE));
        }

        @Test
        @DisplayName("convert_type은 NONE이 반환된다.")
        void shouldReturnNone() {
            AttachmentCreateCommand givenCommand = this.builder.build();

            EmailAttachment attachment = EmailAttachment.of(sendMessage, givenCommand);

            assertThat(attachment.getConvertType()).isEqualTo(ConvertTypeEnum.NONE);
        }

        @Test
        @DisplayName("file_key와 file_key_template 모두 존재하면, file_key만 저장된다.")
        void shouldReturnFileKey_whenFileKeyAndFileKeyTemplateAreNotNull() {
            AttachmentCreateCommand givenCommand = builder
                    .fileKey("template.html")
                    .fileKeyTemplate("/template/${customer_id}/${target_id}.html")
                    .build();

            EmailAttachment message = EmailAttachment.of(sendMessage, givenCommand);

            assertThat(message.getFileKey()).isEqualTo(givenCommand.fileKey());
            assertThat(message.getFileKeyTemplate()).isNull();
        }

        @Test
        @DisplayName("file_key가 존재할 때, file_size가 NULL이면 익셉션이 발생한다.")
        void shouldThrowException_whenFileKeyFormatIsNull() {
            AttachmentCreateCommand givenCommand = builder
                    .fileKey("template.html")
                    .fileSize(null)
                    .build();
            EmailSendMessage sendMessage = mock(EmailSendMessage.class);

            RequiredException expect = RequiredException.fieldOf("file_size");

            assertThatThrownBy(() -> EmailAttachment.of(sendMessage, givenCommand))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("file_key_template이 존재할 때 fileSize는 NULL이다.")
        void shouldReturnFileSizeNull_whenFileKeyTemplateExists() {
            AttachmentCreateCommand givenCommand = builder
                    .fileKey(null)
                    .fileKeyTemplate("${variable}.html")
                    .fileSize(9999L)
                    .build();

            EmailSendMessage sendMessage = mock(EmailSendMessage.class);

            EmailAttachment attachment = EmailAttachment.of(sendMessage, givenCommand);

            assertThat(attachment.getFileSize()).isNull();
        }
    }

    @Nested
    @DisplayName("CONVERT_TYPE이 PDF일 때")
    class WhenConvertTypeIsPdf {
        private EmailAttachmentCreateCommandBuilder builder;
        private EmailSendMessage sendMessage;

        @BeforeEach
        void setUp() {
            this.sendMessage = mock(EmailSendMessage.class);
             this.builder = EmailAttachmentCreateCommandBuilder.builder()
                    .convertType(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.PDF))
                     .fileKeyTemplate("template.pdf");
        }
        @Test
        @DisplayName("convert_type은 PDF가 반환된다.")
        void shouldReturnPdf() {
            AttachmentCreateCommand givenCommand = builder.build();

            EmailAttachment message = EmailAttachment.of(sendMessage, givenCommand);

            assertThat(message.getConvertType()).isEqualTo(ConvertTypeEnum.PDF);
        }

        @Test
        @DisplayName("file_key가 존재하지 않으면 익셉션이 발생한다.")
        void shouldThrowException_whenFileKeyIsNull() {
            AttachmentCreateCommand givenCommand = builder.fileKey(null)
                    .build();

            RequiredException expect = RequiredException.fieldOf("file_key");

            assertThatThrownBy(() -> EmailAttachment.of(sendMessage, givenCommand))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("file_size가 NULL이면 익셉션이 발생한다.")
        void shouldThrowException_whenFileSizeIsNull() {
            AttachmentCreateCommand givenCommand = builder.fileSize(null).build();

            RequiredException expect = RequiredException.fieldOf("file_size");

            assertThatThrownBy(() -> EmailAttachment.of(sendMessage, givenCommand))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("file_key_template이 존재하지 않으면 익셉션이 발생한다.")
        void shouldThrowException_whenFileKeyTemplateDoesNotExist() {
            AttachmentCreateCommand givenCommand = builder
                    .fileKeyTemplate(null)
                    .build();

            RequiredException expect = RequiredException.fieldOf("file_key_template");

            assertThatThrownBy(() -> EmailAttachment.of(sendMessage, givenCommand))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());

        }

        @Test
        @DisplayName("file_key_template의 확장자가 pdf가 아니면 익셉션이 발생한다.")
        void shouldThrowException_whenFileKeyTemplateExtensionIsNotPdf() {
            AttachmentCreateCommand givenCommand = builder.fileKeyTemplate("template.xlsx")
                    .build();

            InvalidFileExtensionException expect = InvalidFileExtensionException.of("pdf");

            assertThatThrownBy(() -> EmailAttachment.of(sendMessage, givenCommand))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
    }

    @Nested
    @DisplayName("CONVERT_TYPE이 HTML일 때")
    class WhenConvertTypeIsHtml {
        private EmailMessageCreateCommandBuilder command =
                EmailMessageCreateCommandBuilder.builder()
                        .fileKeyTemplate("template.html")
                        .convertType(ConvertTypeEnum.HTML);

        @Test
        @DisplayName("convert_type은 HTML이 반환된다.")
        void shouldReturnHtml() {
            AttachmentCreateCommand givenCommand = command.build();

            EmailSendMessage sendMessage = mock(EmailSendMessage.class);
            EmailAttachment message = EmailAttachment.of(sendMessage, givenCommand);

            assertThat(message.getConvertType()).isEqualTo(ConvertTypeEnum.HTML);
        }

        @Test
        @DisplayName("file_key가 존재하지 않으면 익셉션이 발생한다.")
        void shouldThrowException_whenFileKeyIsNull() {
            AttachmentCreateCommand givenCommand = command.fileKey(null)
                    .build();

            RequiredException expect = RequiredException.fieldOf("file_key");

            EmailSendMessage sendMessage = mock(EmailSendMessage.class);
            assertThatThrownBy(() -> EmailAttachment.of(sendMessage, givenCommand))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("file_template_key에는 확장자가 html인 업로드 경로가 생성된다.")
        void shouldReturnUploadKey() {
            AttachmentCreateCommand command = this.command.build();

            EmailSendMessage sendMessage = mock(EmailSendMessage.class);
            EmailAttachment message = EmailAttachment.of(sendMessage, command);

            assertThat(message.getFileKeyTemplate()).endsWith(".html");
        }


        @Test
        @DisplayName("file_size가 NULL이면 익셉션이 발생한다.")
        void shouldThrowException_whenFileSizeIsNull() {
            AttachmentCreateCommand command = this.command.fileSize(null).build();

            RequiredException expect = RequiredException.fieldOf("file_size");

            EmailSendMessage sendMessage = mock(EmailSendMessage.class);
            assertThatThrownBy(() -> EmailAttachment.of(sendMessage, command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("file_key_template의 확장자가 html이 아니면 익셉션이 발생한다.")
        void shouldThrowException_whenFileKeyTemplateExtensionIsNotHtml() {
            AttachmentCreateCommand command = this.command
                    .fileKeyTemplate("template.xlsx").build();

            InvalidFileExtensionException expect = InvalidFileExtensionException.of("html");

            EmailSendMessage sendMessage = mock(EmailSendMessage.class);
            assertThatThrownBy(() -> EmailAttachment.of(sendMessage, command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
    }

    @Nested
    @DisplayName("SECURITY_POLICY가 NULL이 아닐 때")
    class WhenSecurityPolicyIsNotNull {
        private EmailAttachmentCreateCommandBuilder builder;
        private EmailSendMessage sendMessage;

        @BeforeEach
        void setUp() {
            sendMessage = mock(EmailSendMessage.class);
            builder = EmailAttachmentCreateCommandBuilder.builder()
                    .convertType(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.HTML))
                    .encryptionType(EncryptionTypeEnum.ASE128)
                    .passwordHash(PasswordHashEnum.SHA256)
                    .passwordFormat("customer_id");
        }

        @Test
        @DisplayName("convert_type이 NONE이면 security_policy는 존재하지 않는다.")
        void shouldNotExistSecurityPolicy_whenConvertTypeIsNone() {
            AttachmentCreateCommand givenCommand = builder.convertType(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.NONE))
                    .build();

            EmailAttachment attachment = EmailAttachment.of(sendMessage, givenCommand);

            assertThat(attachment.getSecurityPolicy()).isNull();
        }

        @Test
        @DisplayName("encrytion_type이 NULL이면 ASE-256이 반환된다.")
        void shouldReturnEncryptionTypeIsAse256_whenEncryptionTypeIsNull() {
            AttachmentCreateCommand givenCommand = builder
                    .encryptionType(null).build();

            EmailSendMessage sendMessage = mock(EmailSendMessage.class);
            EmailAttachment message = EmailAttachment.of(sendMessage, givenCommand);
            SecurityPolicy expect = message.getSecurityPolicy();

            assertThat(expect.getEncryptionType()).isEqualTo(EncryptionTypeEnum.ASE256);
        }

        @Test
        @DisplayName("password_hash가 NULL이면 SHA-256이 반환된다.")
        void shouldReturnPasswordHashIsSha256_whenPasswordHashIsNull() {
            AttachmentCreateCommand givenCommand = builder
                    .passwordHash(null).build();

            EmailSendMessage sendMessage = mock(EmailSendMessage.class);
            EmailAttachment message = EmailAttachment.of(sendMessage, givenCommand);

            SecurityPolicy expect = message.getSecurityPolicy();

            assertThat(expect.getPasswordHash()).isEqualTo(PasswordHashEnum.SHA256);
        }

        @Test
        @DisplayName("permission_mask가 NULL이면 NONE이 반환된다.")
        void shouldReturnPermissionMaskIsNone_whenPermisionMaskIsNull(){
            AttachmentCreateCommand givenCommand = builder.permissionMask(null).build();

            EmailSendMessage sendMessage = mock(EmailSendMessage.class);
            EmailAttachment message = EmailAttachment.of(sendMessage, givenCommand);
            SecurityPolicy expect = message.getSecurityPolicy();

            assertThat(expect.getPermissionMask()).isEqualTo(PermissionMaskEnum.NONE);
        }
    }

    @Nested
    @DisplayName("유효성 검증 테스트")
    class ValidationTest {
        private EmailSendMessage sendMessage;
        private EmailAttachmentCreateCommandBuilder builder;

        @BeforeEach
        void setUp() {
            this.sendMessage = mock(EmailSendMessage.class);
            this.builder = EmailAttachmentCreateCommandBuilder.builder();
        }
        @Test
        @DisplayName("attachment_name이 NULL이면 익셉션이 발생한다.")
        void shouldThrowException_whenAttachmentNameIsEmpty() {
            AttachmentCreateCommand givenCommand = builder.attachmentName(null).build();

            EmailSendMessage sendMessage = mock(EmailSendMessage.class);

            RequiredException expect = RequiredException.fieldOf("attachment_name");
            assertThatThrownBy(() -> EmailAttachment.of(sendMessage, givenCommand))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("download_name이 NULL이면 익셉션이 발생한다.")
        void shouldThrowException_whenDownloadNameIsNull() {
            AttachmentCreateCommand givenCommand = builder.downloadName(null).build();
            EmailSendMessage sendMessage = mock(EmailSendMessage.class);

            RequiredException expect = RequiredException.fieldOf("download_name");

            assertThatThrownBy(() -> EmailAttachment.of(sendMessage, givenCommand))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("file_key와 file_key_template 모두 NULL이면 익셉션이 발생한다.")
        void shouldThrowException_fileKeyAndFileKeyTemplateAreNull() {
            AttachmentCreateCommand givenCommand = builder.fileKey(null).fileKeyTemplate(null).build();
            EmailSendMessage sendMessage = mock(EmailSendMessage.class);

            RequiredException expect = RequiredException.fieldOf("file_key_template");

            assertThatThrownBy(() -> EmailAttachment.of(sendMessage, givenCommand))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
    }
}