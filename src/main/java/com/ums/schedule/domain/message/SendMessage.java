package com.ums.schedule.domain.message;

import com.ums.schedule.application.ums.common.message.model.SendMessageCreateCommand;
import com.ums.schedule.domain.exception.email.MessageTypeNotFoundException;
import com.ums.schedule.domain.exception.email.SendMessageMissingException;
import com.ums.schedule.domain.sendrequest.converter.UuidBinaryConverter;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.exception.request.SendRequestNotFoundException;
import com.ums.schedule.common.code.message.MessageType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.util.StringUtils;

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
        message.assignSendRequest(command.sendRequest());
        message.assignMessageTypeAndAdvertisingPrefix(command.messageType(), command.advertisingPrefix());
        return message;
    }

    private void assignMessageTypeAndAdvertisingPrefix(MessageType messageType, String messagePrefix) {
        if(MessageType.ADVERTISE == messageType) {
            if (!StringUtils.hasText(messagePrefix)) {
                throw SendMessageMissingException.of("advertising_prefix");
            }
            this.messagePrefix = messagePrefix;
        }
        assignMessageType(messageType);
    }

    private void assignMessageType(MessageType messageType) {
        this.messageType = Optional.ofNullable(messageType)
                .orElseThrow(MessageTypeNotFoundException::of);
    }


    private void assignSendRequest(SendRequest sendRequest) {
        if(sendRequest == null) {
            throw SendRequestNotFoundException.of();
        }
        this.sendRequest = sendRequest;
    }

    public String generatePhraseByMessageType(String content) {
        if(messageType == MessageType.ADVERTISE) {
            return "%s %s".formatted(messagePrefix, content);
        }
        return content;
    }
}
