package com.ums.schedule.domain.message;

import com.ums.schedule.application.ums.common.message.model.SendMessageCreateCommand;
import com.ums.schedule.common.code.message.MessageType;
import com.ums.schedule.domain.exception.email.MessageTypeNotFoundException;
import com.ums.schedule.domain.exception.email.SendMessageMissingException;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.exception.request.SendRequestNotFoundException;
import com.ums.schedule.fixture.message.SendMessageCreateCommandBuilder;
import com.ums.schedule.fixture.sendrequest.SendRequestEntityBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class SendMessageTest {
    private SendMessageCreateCommandBuilder builder;

    @BeforeEach
    void setUp() {
        this.builder = SendMessageCreateCommandBuilder
                .builder()
                .sendRequest(mock(SendRequest.class))
        ;
    }
    @Nested
    @DisplayName("메시지 생성")
    class WhenSendMessageCreate {
        @Test
        @DisplayName("SendRequest가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenSendRequestDoesNotExist() {
            SendMessageCreateCommand command = builder.sendRequest(null).build();

            SendRequestNotFoundException expect = SendRequestNotFoundException.of();

            assertThatThrownBy(() -> SendMessage.of(command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("메시지 타입이 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenMessageTypeDoesNotExist() {
            SendMessageCreateCommand command = builder.messageType(null).build();
            MessageTypeNotFoundException expect = MessageTypeNotFoundException.of();

            assertThatThrownBy(() -> SendMessage.of(command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("메시지 타입이 존재하면, 저장된다.")
        void shouldSaveMessageType() {
            SendMessageCreateCommand command = builder.messageType(MessageType.NONE).build();
            SendMessage message = SendMessage.of(command);
            assertThat(message.getMessageType()).isEqualTo(MessageType.NONE);
        }

        @Test
        @DisplayName("메시지 타입이 ADVERTISE이고, 문구가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenMessageTypeIsAdvertiseAndMessagePrefixDoesNotExist() {
            SendMessageCreateCommand command = builder.messageType(MessageType.ADVERTISE)
                    .messagePrefix("")
                    .build();

            SendMessageMissingException expect =
                    SendMessageMissingException.of("advertising_prefix");

            assertThatThrownBy(() -> SendMessage.of(command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("메시지 타입이 ADVERTISE이고, 문구가 존재하면 저장된다.")
        void shouldSaveMessagePrefix() {
            SendMessageCreateCommand command = builder.messageType(MessageType.ADVERTISE)
                            .messagePrefix("(광고)")
                            .build();
            SendMessage message = SendMessage.of(command);

            assertThat(message.getMessagePrefix()).isEqualTo("(광고)");
        }

        @Test
        @DisplayName("메시지 타입이 NONE이고, 문구가 존재해도 저장되지 않는다.")
        void shouldNotSaveMessagePrefix_whenMessageTypeIsNone() {
            SendMessageCreateCommand command = builder.messageType(MessageType.NONE)
                    .messagePrefix("(광고)")
                    .build();

            SendMessage message = SendMessage.of(command);

            assertThat(message.getMessagePrefix()).isNull();
        }

        @Test
        @DisplayName("SendRequest와 연관관계가 설정된다.")
        void shouldRelateWithSendRequest() {
            SendRequest sendRequest = SendRequestEntityBuilder.builder().build();
            SendMessageCreateCommand command = builder.sendRequest(sendRequest).build();
        }
    }
}