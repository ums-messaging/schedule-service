package com.ums.schedule.domain.message.email.attachment;

import com.ums.schedule.application.ums.email.attachment.model.AttachmentCreateCommand;
import com.ums.schedule.common.exception.validation.InvalidFileExtensionException;
import com.ums.schedule.common.exception.validation.RequiredException;
import com.ums.schedule.common.code.email.ConvertTypeEnum;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.exception.email.EmailAttachmentMissingException;
import com.ums.schedule.domain.exception.email.EmailSendMessageNotFoundException;
import com.ums.schedule.common.code.email.AttachmentType;
import com.ums.schedule.domain.message.email.SecurityMailPolicy;
import com.ums.schedule.domain.sendrequest.message.email.EmailSendMessageBuilder;
import com.ums.schedule.fixture.email.attachment.EmailAttachmentCreateContextBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

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

        @BeforeEach
        void setUp() {
            contextBuilder
                    .convertType(ConvertTypeEnum.NONE)
                    .fileKeyMap(Map.of(
                            AttachmentType.DIRECT,
                            "attachment.html"
                    )).fileSize(10L)
            ;
        }

        @Test
        @DisplayName("변환 타입은 NONE이 반환된다.")
        void shouldReturnNone() {
            AttachmentCreateCommand context = contextBuilder.build();

            EmailAttachment result = EmailAttachment.of(context);

            assertThat(result.getConvertType()).isEqualTo(ConvertTypeEnum.NONE);
        }

        @Nested
        @DisplayName("첨부파일 타입이 DIRECT일 때")
        class WhenAttachmentTypeIsDirect {
            @BeforeEach
            void setUp() {
                contextBuilder
                        .fileKeyMap(
                                Map.of(
                                        AttachmentType.DIRECT,
                                        "attachment.html"
                                )
                        )
                        .fileSize(10L);
            }
            @Test
            @DisplayName("첨부파일 타입이 DIRECT이면, 변환된 첨부파일의 키가 file_key로 저장된다.")
            void shouldReturnConvertedAttachmentFileKey() {
                AttachmentCreateCommand context = contextBuilder
                        .build();

                EmailAttachment result = EmailAttachment.of(context);

                assertThat(result.getFileKey()).isEqualTo("attachment.html");
            }

            @Test
            @DisplayName("변환된 첨부파일의 키가 존재하지 않으면 예외가 발생한다.")
            void shouldThrowException_whenFileKeyDoesNotExist() {
                AttachmentCreateCommand context = contextBuilder
                        .fileKeyMap(Map.of(
                                AttachmentType.DIRECT, ""
                        ))
                        .build();

                EmailAttachmentMissingException expect = EmailAttachmentMissingException.of("file_key");

                assertThatThrownBy(() -> EmailAttachment.of(context))
                        .isInstanceOf(expect.getClass())
                        .hasMessage(expect.getMessage());
            }
            @Test
            @DisplayName("파일 크기가 존재하지 않으면 예외가 발생한다.")
            void shouldThrowException_whenFileSizeIsNull() {
                AttachmentCreateCommand context = contextBuilder
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
                contextBuilder
                        .fileKeyMap(
                                Map.of(
                                        AttachmentType.TEMPLATE,
                                        "${attachment}.html"
                                )
                        );
            }

            @Test
            @DisplayName("변환된 첨부파일의 키가 file_key_template으로 저장된다.")
            void shouldThrowException_whenFileKeyFormatIsNull() {
                AttachmentCreateCommand context = contextBuilder
                        .build();

                EmailAttachment result = EmailAttachment.of(context);

                assertThat(result.getFileKeyTemplate()).isEqualTo("${attachment}.html");
            }

            @Test
            @DisplayName("변환된 첨부파일의 키가 존재하지 않으면 예외가 발생한다.")
            void shouldThrowException_whenConvertedAttachmentKeyDoesNotExist() {
                AttachmentCreateCommand context = contextBuilder
                        .fileKeyMap(
                                Map.of(AttachmentType.TEMPLATE, "")
                        )
                        .build();

                EmailAttachmentMissingException expect = EmailAttachmentMissingException.of("file_key_template");

                assertThatThrownBy(() -> EmailAttachment.of(context))
                        .isInstanceOf(expect.getClass())
                        .hasMessage(expect.getMessage());
            }

            @Test
            @DisplayName("파일 크기가 존재하지 않으면 NULL을 반환한다.")
            void shouldReturnFileSizeNull_whenFileSizeDoesNotExist() {
                AttachmentCreateCommand context = contextBuilder
                        .fileSize(null)
                        .build();

                EmailAttachment result = EmailAttachment.of(context);

                assertThat(result.getFileSize()).isNull();
            }
        }

        @Test
        @DisplayName("보안 정책은 존재하지 않는다.")
        void shouldReturnNullSecurityMailPolicy() {
            AttachmentCreateCommand context = contextBuilder
                    .convertType(ConvertTypeEnum.NONE)
                    .fileKeyMap(Map.of(
                            AttachmentType.DIRECT,
                            "attachment.html"
                    ))
                    .securityMail(mock(SecurityMailPolicy.class))
                    .build();

            EmailAttachment result = EmailAttachment.of(context);

            assertThat(result.getSecurityPolicy()).isNull();
        }

        @Test
        @DisplayName("파일 키 정보가 존재하지 않으면, 예외가 발생한다.")
        void shouldThrowException_whenFileKeyMapDoesNotExist() {
            AttachmentCreateCommand context = contextBuilder
                    .fileKeyMap(null).build();

            EmailAttachmentMissingException expect = EmailAttachmentMissingException.of("file_key or file_key_template");

            assertThatThrownBy(() -> EmailAttachment.of(context))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
    }


    @Nested
    @DisplayName("CONVERT_TYPE이 HTML일 때")
    class WhenConvertTypeIsHtml {

        @BeforeEach
        void setUp() {
            Map<AttachmentType, String> bodyKeyMap = Map.of(
                    AttachmentType.DIRECT, "body.html",
                    AttachmentType.TEMPLATE, "${template}.html"
            );
            contextBuilder = EmailAttachmentCreateContextBuilder.builder()
                    .sendMessage(mock(EmailSendMessage.class))
                    .convertType(ConvertTypeEnum.HTML)
                    .fileKeyMap(bodyKeyMap)
                    ;
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
            AttachmentCreateCommand context = contextBuilder.build();

            EmailAttachment result = EmailAttachment.of(context);

            assertThat(result.getFileKey()).isEqualTo("body.html");
        }

        @Test
        @DisplayName("파일 키 템플릿은 변환된 첨부파일 키로 반환된다.")
        void shouldReturnConvertedAttachmentKey() {
            AttachmentCreateCommand context = contextBuilder
                    .sendMessage(mock(EmailSendMessage.class))
                    .build();

            EmailAttachment result = EmailAttachment.of(context);

            assertThat(result.getFileKeyTemplate()).isEqualTo("${template}.html");
        }

        @Test
        @DisplayName("파일 키가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenFileKeyIsEmpty() {
            contextBuilder = contextBuilder
                    .fileKeyMap(
                            Map.of(AttachmentType.TEMPLATE, "${template}.html")
                    );

            EmailAttachmentMissingException expect = EmailAttachmentMissingException.of("file_key");

            assertThatThrownBy(() -> EmailAttachment.of(contextBuilder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("변환된 첨부파일 키가 존재하지 않으면 예외가 발생한다.")
        void shouldReturnUploadKey() {
            contextBuilder = contextBuilder
                    .fileKeyMap(Map.of(
                            AttachmentType.DIRECT, "body.html"
                    ));

            EmailAttachmentMissingException expect = EmailAttachmentMissingException.of("file_key_template");

            assertThatThrownBy(() -> EmailAttachment.of(contextBuilder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("파일 크기가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenFileSizeIsNull() {
            contextBuilder = contextBuilder
                    .fileSize(null);

            EmailAttachmentMissingException expect = EmailAttachmentMissingException.of("file_size");

            assertThatThrownBy(() -> EmailAttachment.of(contextBuilder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("파일 키의 확장자가 HTML이 아니면 예외가 발생한다.")
        void shouldThrowException_whenFileKeyExtensionIsNotHtml() {
            contextBuilder = contextBuilder
                    .fileKeyMap(
                            Map.of(
                                    AttachmentType.DIRECT, "body.pdf",
                                    AttachmentType.TEMPLATE, "${template}.html"
                            )
                    );

            InvalidFileExtensionException expect = InvalidFileExtensionException.of("html");

            assertThatThrownBy(() -> EmailAttachment.of(contextBuilder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("변환된 첨부파일 키의 확장자가 HTML이 아니면 익셉션이 발생한다.")
        void shouldThrowException_whenFileKeyTemplateExtensionIsNotHtml() {
            contextBuilder = contextBuilder
                    .fileKeyMap(
                            Map.of(
                                    AttachmentType.DIRECT, "body.html",
                                    AttachmentType.TEMPLATE, "${template}.pdf"
                            )
                    );

            InvalidFileExtensionException expect = InvalidFileExtensionException.of("html");

            assertThatThrownBy(() -> EmailAttachment.of(contextBuilder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("보안 정책이 존재하면, 보안 정책 정보가 저장된다.")
        void shouldReturnNotNull_whenSecurityMailPolicyExists() {
            contextBuilder = contextBuilder
                    .securityMail(mock(SecurityMailPolicy.class));

            EmailAttachment attachment = EmailAttachment.of(contextBuilder.build());

            assertThat(attachment.getSecurityPolicy()).isNotNull();
        }

        @Test
        @DisplayName("파일 키 정보가 존재하지 않으면, 예외가 발생한다.")
        void shouldThrowException_whenFileKeyMapDoesNotExist() {
            AttachmentCreateCommand context = contextBuilder
                    .fileKeyMap(null).build();

            EmailAttachmentMissingException expect = EmailAttachmentMissingException.of("file_key and file_key_template");

            assertThatThrownBy(() -> EmailAttachment.of(context))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
    }

    @Nested
    @DisplayName("CONVERT_TYPE이 PDF일 때")
    class WhenConvertTypeIsPdf {

        @BeforeEach
        void setUp() {
            contextBuilder = EmailAttachmentCreateContextBuilder.builder()
                    .sendMessage(mock(EmailSendMessage.class))
                    .convertType(ConvertTypeEnum.PDF)
                    .fileKeyMap(
                        Map.of(
                                AttachmentType.DIRECT, "body.html",
                                AttachmentType.TEMPLATE, "${template}.pdf"
                        )
                    );
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
            AttachmentCreateCommand context = contextBuilder.build();

            EmailAttachment result = EmailAttachment.of(context);

            assertThat(result.getFileKey()).isEqualTo("body.html");
        }

        @Test
        @DisplayName("파일 키 템플릿은 변환된 첨부파일 키로 반환된다.")
        void shouldReturnConvertedAttachmentKey() {
            AttachmentCreateCommand context = contextBuilder
                    .sendMessage(mock(EmailSendMessage.class))
                    .build();

            EmailAttachment result = EmailAttachment.of(context);

            assertThat(result.getFileKeyTemplate()).isEqualTo("${template}.pdf");
        }

        @Test
        @DisplayName("파일 키가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenFileKeyIsEmpty() {
            contextBuilder = contextBuilder
                    .fileKeyMap(
                            Map.of(
                                    AttachmentType.TEMPLATE, "${template}.pdf"
                            )
                    );

            EmailAttachmentMissingException expect = EmailAttachmentMissingException.of("file_key");

            assertThatThrownBy(() -> EmailAttachment.of(contextBuilder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("변환된 첨부파일 키가 존재하지 않으면 예외가 발생한다.")
        void shouldReturnUploadKey() {
            contextBuilder = contextBuilder
                    .fileKeyMap(
                            Map.of(
                                    AttachmentType.DIRECT, "body.html"
                            )
                    );

            EmailAttachmentMissingException expect = EmailAttachmentMissingException.of("file_key_template");

            assertThatThrownBy(() -> EmailAttachment.of(contextBuilder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("파일 크기가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenFileSizeIsNull() {
            contextBuilder = contextBuilder
                    .fileSize(null);

            EmailAttachmentMissingException expect = EmailAttachmentMissingException.of("file_size");

            assertThatThrownBy(() -> EmailAttachment.of(contextBuilder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("파일 키의 확장자가 HTML이 아니면 예외가 발생한다.")
        void shouldThrowException_whenFileKeyExtensionIsNotHtml() {
            contextBuilder = contextBuilder
                    .fileKeyMap(
                            Map.of(
                                AttachmentType.DIRECT, "body.pdf",
                                AttachmentType.TEMPLATE, "${template}.pdf"
                            )
                    );

            InvalidFileExtensionException expect = InvalidFileExtensionException.of("html");

            assertThatThrownBy(() -> EmailAttachment.of(contextBuilder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("변환된 첨부파일 키의 확장자가 PDF가 아니면 익셉션이 발생한다.")
        void shouldThrowException_whenFileKeyTemplateExtensionIsNotHtml() {
            contextBuilder = contextBuilder
                    .fileKeyMap(
                            Map.of(
                                    AttachmentType.DIRECT, "body.html",
                                    AttachmentType.TEMPLATE, "${template}.xlsx"
                            )
                    );

            InvalidFileExtensionException expect = InvalidFileExtensionException.of("pdf");

            assertThatThrownBy(() -> EmailAttachment.of(contextBuilder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("보안 정책이 존재하면, 보안 정책 정보가 저장된다.")
        void shouldReturnNotNull_whenSecurityMailPolicyExists() {
            contextBuilder = contextBuilder
                    .securityMail(mock(SecurityMailPolicy.class));

            EmailAttachment attachment = EmailAttachment.of(contextBuilder.build());

            assertThat(attachment.getSecurityPolicy()).isNotNull();
        }

        @Test
        @DisplayName("파일 키 정보가 존재하지 않으면, 예외가 발생한다.")
        void shouldThrowException_whenFileKeyMapDoesNotExist() {
            AttachmentCreateCommand context = contextBuilder
                    .fileKeyMap(null)
                    .build();

            EmailAttachmentMissingException expect = EmailAttachmentMissingException.of("file_key and file_key_template");

            assertThatThrownBy(() -> EmailAttachment.of(context))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
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
            AttachmentCreateCommand context = contextBuilder.sendMessage(null)
                    .build();

            EmailSendMessageNotFoundException expect = EmailSendMessageNotFoundException.of();

            assertThatThrownBy(() -> EmailAttachment.of(context))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("첨부파일 명이 빈 값이면, 익셉션이 발생한다.")
        void shouldThrowException_whenAttachmentNameIsEmpty() {
            AttachmentCreateCommand context = contextBuilder.attachmentName("")
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
            AttachmentCreateCommand context = contextBuilder.downloadName("")
                    .sendMessage(sendMessage)
                    .build();

            RequiredException expect = RequiredException.fieldOf("download_name");

            assertThatThrownBy(() -> EmailAttachment.of(context))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
    }

    @Test
    @DisplayName("이메일 메시지가 존재하면, 양방향 관계가 설정된다.")
    void shouldRelateWithEmailSendMessage() {
        EmailSendMessage sendMessage = EmailSendMessageBuilder.builder().build();
        AttachmentCreateCommand context = contextBuilder.sendMessage(sendMessage)
                .convertType(ConvertTypeEnum.NONE)
                .fileKeyMap(Map.of(
                        AttachmentType.TEMPLATE, "template.html"
                ))
                .build();

        EmailAttachment attachment = EmailAttachment.of(context);

        assertThat(sendMessage.getAttachmentList())
                .anySatisfy(it -> assertThat(it).isSameAs(attachment));
    }
}