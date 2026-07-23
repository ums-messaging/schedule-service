package com.ums.schedule.domain.message.email;

import com.ums.schedule.application.ums.email.message.model.EmailMessageCreateContext;
import com.ums.schedule.application.ums.email.message.provider.EmailMessageContext;
import com.ums.schedule.common.code.email.EmailRequiredValue;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.message.email.exception.EmailMessageValueMissingException;
import com.ums.schedule.domain.request.message.SendMessageBuilder;
import com.ums.schedule.common.code.message.MessageType;
import com.ums.schedule.fixture.email.message.EmailMessageContextBuilder;
import com.ums.schedule.domain.request.SendRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;


public class EmailSendMessageTest {
    private SendMessageBuilder builder;
    private EmailMessageCreateContext context;

    @BeforeEach
    void setUp() {
        givenSendMessage();
    }

    private void givenSendMessage() {
        SendRequest sendRequest = mock(SendRequest.class);
        builder = SendMessageBuilder.builder()
                .sendRequest(sendRequest)
                .messageType(MessageType.NONE)
                .messagePrefix("(광고)");
    }

    @Nested
    @DisplayName("이메일 메시지 생성")
    class WhenEmailSendMessageCreate {
        private EmailMessageContextBuilder contextBuilder;

        @BeforeEach
        void setUp() {
            contextBuilder = EmailMessageContextBuilder.builder()
                    .title("이메일 제목")
                    .bodyKey("body.html");
        }

        @Test
        @DisplayName("header_key가 존재하면 저장된다.")
        void shouldSaveHeaderKey() {
            EmailMessageContext context = contextBuilder.headerKey("header.html").build();

            EmailSendMessage message = EmailSendMessage.of(builder.build(), context);

            assertThat(message.getHeaderTemplateKey()).isEqualTo("header.html");
        }

        @Test
        @DisplayName("header_key가 존재하지 않으면 저장되지 않는다.")
        void shouldNotSaveHeaderKey() {
            EmailMessageContext context = contextBuilder.headerKey(null).build();

            EmailSendMessage message = EmailSendMessage.of(builder.build(), context);

            assertThat(message.getHeaderTemplateKey()).isNull();
        }

        @Test
        @DisplayName("body_key가 존재하면 저장된다.")
        void shouldSaveBodyKey() {
            EmailMessageContext context = contextBuilder.bodyKey("body.html").build();

            EmailSendMessage message = EmailSendMessage.of(builder.build(), context);

            assertThat(message.getBodyTemplateKey()).isEqualTo("body.html");
        }

        @Test
        @DisplayName("body_key가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenBodyKeyDoesNotExist() {
            EmailMessageContext context = contextBuilder.bodyKey(null).build();

            EmailMessageValueMissingException expect = EmailMessageValueMissingException.of(EmailRequiredValue.BODY_TEMPLATE_KEY);

            assertThatThrownBy(() -> EmailSendMessage.of(builder.build(), context))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("footer_key가 존재하면 저장된다.")
        void shouldSaveFooterKey() {
            EmailMessageContext context = contextBuilder.footerKey("footer.html").build();

            EmailSendMessage message = EmailSendMessage.of(builder.build(), context);

            assertThat(message.getFooterTemplateKey()).isEqualTo("footer.html");
        }

        @Test
        @DisplayName("footer_key가 존재하지 않으면 저장되지 않는다.")
        void shouldNotSaveFooterKey() {
            EmailMessageContext context = contextBuilder.footerKey(null).build();

            EmailSendMessage message = EmailSendMessage.of(builder.build(), context);

            assertThat(message.getFooterTemplateKey()).isNull();
        }

        @Test
        @DisplayName("이미지 경로가 존재하면, 저장된다.")
        void shouldSaveImageDir() {
            EmailMessageContext context = contextBuilder.imageDir("/message/email/images").build();

            EmailSendMessage message = EmailSendMessage.of(builder.build(), context);

            assertThat(message.getImageDir()).isEqualTo("/message/email/images");
        }

        @Test
        @DisplayName("이미지 경로가 존재하지 않으면, 저장되지 않는다.")
        void shouldNotSaveImageDir() {
            EmailMessageContext context = contextBuilder.imageDir(null).build();

            EmailSendMessage message = EmailSendMessage.of(builder.build(), context);

            assertThat(message.getImageDir()).isNull();
        }

        @Test
        @DisplayName("SendMessage가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenSendMessageDoesNotExist() {
            EmailMessageContext context = contextBuilder.build();

            assertThatThrownBy(() -> EmailSendMessage.of(null, context))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("send_message");
        }

        @Test
        @DisplayName("SendMessage가 존재하면 저장된다.")
        void shouldSaveSendMessage() {
            EmailMessageContext context = contextBuilder.build();
            EmailSendMessage message = EmailSendMessage.of(builder.build(), context);
            assertThat(message.getSendMessage()).isNotNull();
        }

        @Test
        @DisplayName("SendMessage의 메시지 타입이 ADVERTISE이면, 제목 앞에 '(광고)' 표시가 붙는다.")
        void shouldPrependAdvertisePrefix_whenMessageTypeIsAdvertise() {
            EmailMessageContext context = contextBuilder.title("hello world!").build();
            SendMessage sendMessage = builder.messageType(MessageType.ADVERTISE).build();
            EmailSendMessage message = EmailSendMessage.of(sendMessage, context);

            assertThat(message.getSubject()).isEqualTo("(광고) hello world!");
        }

        @Test
        @DisplayName("SendMessage의 메시지 타입이 NONE이면, 제목 그대로 저장된다.")
        void shouldSaveSubject_whenMessageTypeIsNone() {
            EmailMessageContext context = contextBuilder.title("hello world!").build();
            SendMessage sendMessage = builder.messageType(MessageType.NONE).build();
            EmailSendMessage message = EmailSendMessage.of(sendMessage, context);

            assertThat(message.getSubject()).isEqualTo("hello world!");
        }

        @Test
        @DisplayName("제목이 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenTitleDoesNotExist() {
            EmailMessageContext context = contextBuilder.title(null).build();
            SendMessage sendMessage = builder.messageType(MessageType.NONE).build();

            assertThatThrownBy(() -> EmailSendMessage.of(sendMessage, context))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("title");
        }
    }
}
