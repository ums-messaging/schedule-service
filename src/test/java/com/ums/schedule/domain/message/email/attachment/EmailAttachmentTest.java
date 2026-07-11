package com.ums.schedule.domain.message.email.attachment;

import com.ums.schedule.application.ums.email.attachment.model.EmailAttachmentCreateContext;
import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.common.exception.validation.InvalidFileExtensionException;
import com.ums.schedule.common.exception.validation.RequiredException;
import com.ums.schedule.domain.message.email.code.ConvertTypeEnum;
import com.ums.schedule.domain.message.exception.EmailSendMessage;
import com.ums.schedule.domain.message.exception.EmailAttachmentMissingException;
import com.ums.schedule.domain.message.exception.EmailSendMessageNotFoundException;
import com.ums.schedule.domain.message.email.EmailAttachment;
import com.ums.schedule.domain.message.email.code.AttachmentType;
import com.ums.schedule.domain.message.email.SecurityMailPolicy;
import com.ums.schedule.fixture.email.attachment.EmailAttachmentCreateContextBuilder;
import com.ums.schedule.fixture.email.convert.ConvertedAttachmentBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class EmailAttachmentTest {
    private EmailAttachmentCreateContextBuilder contextBuilder;

    @BeforeEach
    void setUp() {
        this.contextBuilder = EmailAttachmentCreateContextBuilder.
                builder()
                .sendMessage(mock(EmailSendMessage.class));
    }

    @Nested
    @DisplayName("변환 타입이 NONE일 때")
    class WhenConvertTypeIsNone {
        private ConvertedAttachmentBuilder convertedBuilder;

        @BeforeEach
        void setUp() {
            convertedBuilder = ConvertedAttachmentBuilder.builder()
                    .convertType(ConvertTypeEnum.NONE)
                    .attachmentType(AttachmentType.DIRECT)
                    .key("attachment.html")
            ;
        }

        @Test
        @DisplayName("변환 타입은 NONE이 반환된다.")
        void shouldReturnNone() {
            ConvertedAttachment attachment = convertedBuilder.build();
            EmailAttachmentCreateContext context = contextBuilder.convertedAttachment(attachment).build();

            EmailAttachment result = EmailAttachment.of(context);

            assertThat(result.getConvertType()).isEqualTo(ConvertTypeEnum.NONE);
        }

        @Nested
        @DisplayName("첨부파일 타입이 DIRECT일 때")
        class WhenAttachmentTypeIsDirect {
            @BeforeEach
            void setUp() {
                convertedBuilder = convertedBuilder
                        .attachmentType(AttachmentType.DIRECT)
                        .key("body.html");
                contextBuilder.fileSize(10L);
            }
            @Test
            @DisplayName("첨부파일 타입이 DIRECT이면, 변환된 첨부파일의 키가 file_key로 저장된다.")
            void shouldReturnConvertedAttachmentFileKey() {
                ConvertedAttachment attachment = convertedBuilder.build();

                EmailAttachmentCreateContext context = contextBuilder
                        .convertedAttachment(attachment)
                        .build();

                EmailAttachment result = EmailAttachment.of(context);

                assertThat(result.getFileKey()).isEqualTo("body.html");
            }

            @Test
            @DisplayName("변환된 첨부파일의 키가 존재하지 않으면 예외가 발생한다.")
            void shouldThrowException_whenFileKeyDoesNotExist() {
                ConvertedAttachment attachment = convertedBuilder
                        .key("")
                        .build();
                EmailAttachmentCreateContext context = contextBuilder
                        .convertedAttachment(attachment)
                        .build();

                EmailAttachmentMissingException expect = EmailAttachmentMissingException.of("file_key");

                assertThatThrownBy(() -> EmailAttachment.of(context))
                        .isInstanceOf(expect.getClass())
                        .hasMessage(expect.getMessage());
            }
            @Test
            @DisplayName("파일 크기가 존재하지 않으면 예외가 발생한다.")
            void shouldThrowException_whenFileSizeIsNull() {
                ConvertedAttachment attachment = convertedBuilder.build();
                EmailAttachmentCreateContext context = contextBuilder
                        .convertedAttachment(attachment)
                        .fileSize(null)
                        .build();

                EmailAttachmentMissingException expect = EmailAttachmentMissingException.of("file_size");

                assertThatThrownBy(() -> EmailAttachment.of(context))
                        .isInstanceOf(expect.getClass())
                        .hasMessage(expect.getMessage());

            }
        }

        @Nested
        @DisplayName("첨부파일 타입이 TEMPLATE일 때")
        class WhenAttachmentTypeIsTemplate {
            @BeforeEach
            void setUp() {
                convertedBuilder = convertedBuilder
                        .attachmentType(AttachmentType.TEMPLATE)
                        .key("body.html");
            }

            @Test
            @DisplayName("변환된 첨부파일의 키가 file_key_template으로 저장된다.")
            void shouldThrowException_whenFileKeyFormatIsNull() {
                ConvertedAttachment attachment = convertedBuilder
                        .build();
                EmailAttachmentCreateContext context = contextBuilder
                        .convertedAttachment(attachment)
                        .build();

                EmailAttachment result = EmailAttachment.of(context);

                assertThat(result.getFileKeyTemplate()).isEqualTo("body.html");
            }

            @Test
            @DisplayName("변환된 첨부파일의 키가 존재하지 않으면 예외가 발생한다.")
            void shouldThrowException_whenConvertedAttachmentKeyDoesNotExist() {
                ConvertedAttachment attachment = convertedBuilder
                        .key("")
                        .build();
                EmailAttachmentCreateContext context = contextBuilder
                        .convertedAttachment(attachment)
                        .build();

                EmailAttachmentMissingException expect = EmailAttachmentMissingException.of("file_key_template");

                assertThatThrownBy(() -> EmailAttachment.of(context))
                        .isInstanceOf(expect.getClass())
                        .hasMessage(expect.getMessage());
            }

            @Test
            @DisplayName("파일 크기가 존재하지 않으면 NULL을 반환한다.")
            void shouldReturnFileSizeNull_whenFileSizeDoesNotExist() {
                ConvertedAttachment attachment = convertedBuilder
                        .attachmentType(AttachmentType.TEMPLATE)
                        .key("body.html")
                        .build();
                EmailAttachmentCreateContext context = contextBuilder
                        .convertedAttachment(attachment)
                        .fileSize(null)
                        .build();

                EmailAttachment result = EmailAttachment.of(context);

                assertThat(result.getFileSize()).isNull();
            }
        }

        @Test
        @DisplayName("보안 정책은 존재하지 않는다.")
        void shouldReturnNullSecurityMailPolicy() {
            ConvertedAttachment attachment = convertedBuilder
                    .key("body.html")
                    .build();
            EmailAttachmentCreateContext context = contextBuilder
                    .convertedAttachment(attachment)
                    .securityMailPolicy(mock(SecurityMailPolicy.class))
                    .build();

            EmailAttachment result = EmailAttachment.of(context);

            assertThat(result.getSecurityPolicy()).isNull();
        }
    }


    @Nested
    @DisplayName("CONVERT_TYPE이 HTML일 때")
    class WhenConvertTypeIsHtml {
        private ConvertedAttachmentBuilder convertedBuilder;

        @BeforeEach
        void setUp() {
            convertedBuilder = ConvertedAttachmentBuilder.builder()
                    .convertType(ConvertTypeEnum.HTML)
                    .attachmentType(AttachmentType.TEMPLATE)
                    .key("body.html");
            contextBuilder = EmailAttachmentCreateContextBuilder.builder()
                    .sendMessage(mock(EmailSendMessage.class))
                    .convertedAttachment(convertedBuilder.build());
        }

        @Test
        @DisplayName("변환 타입은 HTML을 반환한다.")
        void shouldReturnHtml() {
            EmailAttachment result = EmailAttachment.of(contextBuilder.build());

            assertThat(result.getConvertType()).isEqualTo(ConvertTypeEnum.HTML);
        }

        @Test
        @DisplayName("파일 키는 파일 정보로 반환된다.")
        void shouldReturnFileKey() {
            EmailAttachmentCreateContext context = contextBuilder.fileKey("body.html").build();

            EmailAttachment result = EmailAttachment.of(context);

            assertThat(result.getFileKey()).isEqualTo("body.html");
        }

        @Test
        @DisplayName("파일 키 템플릿은 변환된 첨부파일 키로 반환된다.")
        void shouldReturnConvertedAttachmentKey() {
            ConvertedAttachment attachment = ConvertedAttachmentBuilder.builder().key("template.html").build();
            EmailAttachmentCreateContext context = contextBuilder.fileKey("body.html")
                    .sendMessage(mock(EmailSendMessage.class))
                    .convertedAttachment(attachment)
                    .build();

            EmailAttachment result = EmailAttachment.of(context);

            assertThat(result.getFileKey()).isEqualTo("template.html");
        }

        @Test
        @DisplayName("파일 키가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenFileKeyIsEmpty() {
            convertedBuilder = convertedBuilder
                    .convertType(ConvertTypeEnum.HTML)
                    .key("body.html");
            contextBuilder = contextBuilder
                    .fileKey("")
                    .convertedAttachment(convertedBuilder.build());

            EmailAttachmentMissingException expect = EmailAttachmentMissingException.of("file_key");

            assertThatThrownBy(() -> EmailAttachment.of(contextBuilder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("변환된 첨부파일 키가 존재하지 않으면 예외가 발생한다.")
        void shouldReturnUploadKey() {
            convertedBuilder = convertedBuilder
                    .key("");
            contextBuilder = contextBuilder
                    .fileKey("body.html")
                    .convertedAttachment(convertedBuilder.build());

            EmailAttachmentMissingException expect = EmailAttachmentMissingException.of("file_key_template");

            assertThatThrownBy(() -> EmailAttachment.of(contextBuilder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("파일 크기가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenFileSizeIsNull() {
            contextBuilder = contextBuilder
                    .fileKey("body.html")
                    .fileSize(null)
                    .convertedAttachment(convertedBuilder.build());

            EmailAttachmentMissingException expect = EmailAttachmentMissingException.of("file_size");

            assertThatThrownBy(() -> EmailAttachment.of(contextBuilder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("파일 키의 확장자가 HTML이 아니면 예외가 발생한다.")
        void shouldThrowException_whenFileKeyExtensionIsNotHtml() {
            contextBuilder = contextBuilder
                    .fileKey("body.pdf")
                    .convertedAttachment(convertedBuilder.build());

            InvalidFileExtensionException expect = InvalidFileExtensionException.of("html");

            assertThatThrownBy(() -> EmailAttachment.of(contextBuilder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("변환된 첨부파일 키의 확장자가 HTML이 아니면 익셉션이 발생한다.")
        void shouldThrowException_whenFileKeyTemplateExtensionIsNotHtml() {
            ConvertedAttachment attachment = convertedBuilder.key("template.pdf").build();
            contextBuilder = contextBuilder
                    .convertedAttachment(attachment);

            InvalidFileExtensionException expect = InvalidFileExtensionException.of("html");

            assertThatThrownBy(() -> EmailAttachment.of(contextBuilder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("보안 정책이 존재하면, 보안 정책 정보가 저장된다.")
        void shouldReturnNotNull_whenSecurityMailPolicyExists() {
            contextBuilder = contextBuilder
                    .convertedAttachment(convertedBuilder.build())
                    .securityMailPolicy(mock(SecurityMailPolicy.class));

            EmailAttachment attachment = EmailAttachment.of(contextBuilder.build());

            assertThat(attachment.getSecurityPolicy()).isNotNull();
        }
    }

    @Nested
    @DisplayName("CONVERT_TYPE이 PDF일 때")
    class WhenConvertTypeIsPdf {
        private ConvertedAttachmentBuilder convertedBuilder;

        @BeforeEach
        void setUp() {
            convertedBuilder = ConvertedAttachmentBuilder.builder()
                    .convertType(ConvertTypeEnum.PDF)
                    .attachmentType(AttachmentType.TEMPLATE)
                    .key("body.pdf");
            contextBuilder = EmailAttachmentCreateContextBuilder.builder()
                    .sendMessage(mock(EmailSendMessage.class))
                    .fileKey("template.html")
                    .convertedAttachment(convertedBuilder.build());
        }

        @Test
        @DisplayName("변환 타입은 PDF를 반환한다.")
        void shouldReturnHtml() {
            EmailAttachment result = EmailAttachment.of(contextBuilder.build());

            assertThat(result.getConvertType()).isEqualTo(ConvertTypeEnum.PDF);
        }

        @Test
        @DisplayName("파일 키는 파일 정보로 반환된다.")
        void shouldReturnFileKey() {
            EmailAttachmentCreateContext context = contextBuilder.fileKey("body.html").build();

            EmailAttachment result = EmailAttachment.of(context);

            assertThat(result.getFileKey()).isEqualTo("body.html");
        }

        @Test
        @DisplayName("파일 키 템플릿은 변환된 첨부파일 키로 반환된다.")
        void shouldReturnConvertedAttachmentKey() {
            ConvertedAttachment attachment = ConvertedAttachmentBuilder.builder().key("template.html").build();
            EmailAttachmentCreateContext context = contextBuilder.fileKey("body.html")
                    .sendMessage(mock(EmailSendMessage.class))
                    .convertedAttachment(attachment)
                    .build();

            EmailAttachment result = EmailAttachment.of(context);

            assertThat(result.getFileKey()).isEqualTo("template.html");
        }

        @Test
        @DisplayName("파일 키가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenFileKeyIsEmpty() {
            convertedBuilder = convertedBuilder
                    .convertType(ConvertTypeEnum.HTML)
                    .key("body.html");
            contextBuilder = contextBuilder
                    .fileKey("")
                    .convertedAttachment(convertedBuilder.build());

            EmailAttachmentMissingException expect = EmailAttachmentMissingException.of("file_key");

            assertThatThrownBy(() -> EmailAttachment.of(contextBuilder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("변환된 첨부파일 키가 존재하지 않으면 예외가 발생한다.")
        void shouldReturnUploadKey() {
            convertedBuilder = convertedBuilder
                    .key("");
            contextBuilder = contextBuilder
                    .fileKey("body.html")
                    .convertedAttachment(convertedBuilder.build());

            EmailAttachmentMissingException expect = EmailAttachmentMissingException.of("file_key_template");

            assertThatThrownBy(() -> EmailAttachment.of(contextBuilder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("파일 크기가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenFileSizeIsNull() {
            contextBuilder = contextBuilder
                    .fileKey("body.html")
                    .fileSize(null)
                    .convertedAttachment(convertedBuilder.build());

            EmailAttachmentMissingException expect = EmailAttachmentMissingException.of("file_size");

            assertThatThrownBy(() -> EmailAttachment.of(contextBuilder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("파일 키의 확장자가 HTML이 아니면 예외가 발생한다.")
        void shouldThrowException_whenFileKeyExtensionIsNotHtml() {
            contextBuilder = contextBuilder
                    .fileKey("body.pdf")
                    .convertedAttachment(convertedBuilder.build());

            InvalidFileExtensionException expect = InvalidFileExtensionException.of("html");

            assertThatThrownBy(() -> EmailAttachment.of(contextBuilder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("변환된 첨부파일 키의 확장자가 PDF가 아니면 익셉션이 발생한다.")
        void shouldThrowException_whenFileKeyTemplateExtensionIsNotHtml() {
            ConvertedAttachment attachment = convertedBuilder.key("template.html").build();
            contextBuilder = contextBuilder
                    .convertedAttachment(attachment);

            InvalidFileExtensionException expect = InvalidFileExtensionException.of("pdf");

            assertThatThrownBy(() -> EmailAttachment.of(contextBuilder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("보안 정책이 존재하면, 보안 정책 정보가 저장된다.")
        void shouldReturnNotNull_whenSecurityMailPolicyExists() {
            contextBuilder = contextBuilder
                    .convertedAttachment(convertedBuilder.build())
                    .securityMailPolicy(mock(SecurityMailPolicy.class));

            EmailAttachment attachment = EmailAttachment.of(contextBuilder.build());

            assertThat(attachment.getSecurityPolicy()).isNotNull();
        }
    }

    @Nested
    @DisplayName("유효성 검증 테스트")
    class ValidationTest {
        private EmailSendMessage sendMessage;

        @BeforeEach
        void setUp() {
            this.sendMessage = mock(EmailSendMessage.class);
        }

        @Test
        @DisplayName("이메일 메시지가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenEmailSendMessageIsNull() {
            EmailAttachmentCreateContext context = contextBuilder.sendMessage(null)
                    .build();

            EmailSendMessageNotFoundException expect = EmailSendMessageNotFoundException.of();

            assertThatThrownBy(() -> EmailAttachment.of(context))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("첨부파일 명이 빈 값이면, 익셉션이 발생한다.")
        void shouldThrowException_whenAttachmentNameIsEmpty() {
            EmailAttachmentCreateContext context = contextBuilder.attachmentName("")
                    .sendMessage(sendMessage)
                    .build();

            RequiredException expect = RequiredException.fieldOf("attachment_name");

            assertThatThrownBy(() -> EmailAttachment.of(context))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("다운로드 명이 빈 값이면, 익셉션이 발생한다.")
        void shouldThrowException_whenDownloadNameIsNull() {
            EmailAttachmentCreateContext context = contextBuilder.downloadName("")
                    .sendMessage(sendMessage)
                    .build();

            RequiredException expect = RequiredException.fieldOf("download_name");

            assertThatThrownBy(() -> EmailAttachment.of(context))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

    }
}