package com.ums.schedule.domain.message.email.attachment;

import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.common.code.api.AttachmentErrorCode;
import com.ums.schedule.common.code.email.AttachmentType;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.exception.AttachmentPolicyViolationException;
import com.ums.schedule.domain.request.message.email.EmailSendMessageBuilder;
import com.ums.schedule.fixture.email.attachment.AttachmentContextBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class EmailAttachmentTest {
    private EmailSendMessage sendMessage;
    private AttachmentContextBuilder builder;

    @BeforeEach
    void setUp() {
        sendMessage = mock(EmailSendMessage.class);
        builder = AttachmentContextBuilder.builder()
                .type(AttachmentType.DIRECT)
                .fileSize(10000L)
                .key("test.pdf")
                .attachmentName("첨부파일 명")
                .downloadName("다운로드");
    }

    @Nested
    @DisplayName("첨부파일 타입이 DIRECT 일 때")
    class WhenAttachmentTypeIsDirect {
        @BeforeEach
        void setUp() {
            builder = builder.type(AttachmentType.DIRECT);
        }

        @Test
        @DisplayName("파일 크기가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenFileSizeDoesNotExist() {
            AttachmentContext context = builder.fileSize(0L).build();

            assertThatThrownBy(() -> EmailAttachment.of(sendMessage, context))
                    .isInstanceOf(AttachmentPolicyViolationException.class)
                    .extracting(v -> ((AttachmentPolicyViolationException) v).getErrorCode())
                    .isEqualTo(AttachmentErrorCode.FILE_SIZE_EMPTY);
        }
    }

    @Nested
    @DisplayName("첨부파일 타입이 TEMPLATE 일 때")
    class WhenAttachmentTypeIsTemplate {
        @BeforeEach
        void setUp() {
            builder = builder.type(AttachmentType.TEMPLATE);
        }

        @Test
        @DisplayName("파일 키 형식이 유효하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenInvalidFileKeyFormat() {
            AttachmentContext context = builder.key("my_template.pdf").build();

            assertThatThrownBy(() -> EmailAttachment.of(sendMessage, context))
                    .isInstanceOf(AttachmentPolicyViolationException.class)
                    .extracting(v -> ((AttachmentPolicyViolationException) v).getErrorCode())
                    .isEqualTo(AttachmentErrorCode.INVALID_FILE_KEY_TEMPLATE);
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
        @DisplayName("파일 타입이 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenFileTypeDoesNotExist() {
            AttachmentContext context = builder.type(null).build();
            NullPointerException expect = new NullPointerException();

            assertThatThrownBy(() -> EmailAttachment.of(sendMessage, context))
                    .isInstanceOf(expect.getClass())
                    .hasMessage("file_type");
        }

        @Test
        @DisplayName("파일 키가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenFileKeyDoesNotExist() {
            AttachmentContext context = builder.key(null).build();
            NullPointerException expect = new NullPointerException();

            assertThatThrownBy(() -> EmailAttachment.of(sendMessage, context))
                    .isInstanceOf(expect.getClass())
                    .hasMessage("file_key");
        }

        @Test
        @DisplayName("첨부파일 명이 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenAttachmentNameDoesNotExist() {
            AttachmentContext context = builder.attachmentName(null).build();
            NullPointerException expect = new NullPointerException();

            assertThatThrownBy(() -> EmailAttachment.of(sendMessage, context))
                    .isInstanceOf(expect.getClass())
                    .hasMessage("attachment_name");
        }

        @Test
        @DisplayName("다운로드 명이 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenDownloadNameDoesNotExist() {
            AttachmentContext context = builder.downloadName(null).build();
            NullPointerException expect = new NullPointerException();

            assertThatThrownBy(() -> EmailAttachment.of(sendMessage, context))
                    .isInstanceOf(expect.getClass())
                    .hasMessage("download_name");
        }

        @Test
        @DisplayName("이메일 메시지가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenEmailSendMessageIsNull() {
            NullPointerException expect = new NullPointerException();

            assertThatThrownBy(() -> EmailAttachment.of(null, builder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessageContaining("email_send_message");
        }
    }

    @Test
    @DisplayName("이메일 메시지가 존재하면, 양방향 관계가 설정된다.")
    void shouldRelateWithEmailSendMessage() {
        EmailSendMessage sendMessage = EmailSendMessageBuilder.builder().build();

        EmailAttachment attachment = EmailAttachment.of(sendMessage, builder.build());

        assertThat(sendMessage.getAttachments())
                .anySatisfy(it -> assertThat(it).isSameAs(attachment));
    }
}