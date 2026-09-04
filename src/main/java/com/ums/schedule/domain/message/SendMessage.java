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

import java.util.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SendMessage {
    @Id
    @Column
    @GeneratedValue
    @UuidGenerator
    @Convert(converter = UuidBinaryConverter.class)
    private UUID id;

    @Column(name = "template_key", nullable = false)
    private String templateKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false)
    private MessageType messageType;

    private String messagePrefix;

    public static SendMessage of(SendMessageCreateCommand command) {
        SendMessage message = new SendMessage();
        message.assignMessageTypeAndAdvertisingPrefix(command.messageType(), command.advertisingPrefix());
        message.assignTemplateKey(command.templateKey());
        return message;
    }

    private void assignTemplateKey(String templateKey) {
        this.templateKey = Objects.requireNonNull(templateKey, "template_key is required.");
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

    public String generatePhraseByMessageType(String content) {
        if(messageType == MessageType.ADVERTISE) {
            return "%s %s".formatted(messagePrefix, content);
        }
        return content;
    }
}
