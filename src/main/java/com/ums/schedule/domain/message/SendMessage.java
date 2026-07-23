package com.ums.schedule.domain.message;

import com.ums.schedule.application.ums.common.message.model.SendMessageCreateCommand;
import com.ums.schedule.domain.request.converter.UuidBinaryConverter;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.common.code.message.MessageType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SendMessage {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    @GeneratedValue
    @UuidGenerator
    @Convert(converter = UuidBinaryConverter.class)
    private UUID id;

    @Enumerated
    private MessageType messageType;

    private String messagePrefix;

    @ManyToOne
    @JoinColumn(name = "send_request_id", nullable = false)
    private SendRequest sendRequest;

    public static SendMessage of(SendMessageCreateCommand command) {
        SendMessage message = new SendMessage();
        message.assignMessageTypeAndAdvertisingPrefix(command.messageType(), command.advertisingPrefix());
        return message;
    }

    private void assignMessageTypeAndAdvertisingPrefix(MessageType messageType, String messagePrefix) {
        assignMessageType(messageType);
        if(MessageType.ADVERTISE == messageType) {
            this.messagePrefix = Objects.requireNonNull(messagePrefix, "advertise_message_prefix");
        }
    }

    private void assignMessageType(MessageType messageType) {
        this.messageType = Objects.requireNonNull(messageType, "message_type");
    }

    public void assignSendRequest(SendRequest sendRequest) {
        this.sendRequest = Objects.requireNonNull(sendRequest, "send_request");
    }

    public String generatePhraseByMessageType(String content) {
        if(messageType == MessageType.ADVERTISE) {
            return "%s %s".formatted(messagePrefix, content);
        }
        return content;
    }
}
