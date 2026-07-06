package com.ums.schedule.domain.sendrequest.message;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.util.ValidationUtils;
import com.ums.schedule.domain.sendrequest.converter.UuidBinaryConverter;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.exception.SendRequestNotFoundException;
import com.ums.schedule.domain.sendrequest.template.code.TemplateTypeEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Optional;
import java.util.UUID;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SendMessage {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    @GeneratedValue
    @UuidGenerator
    @Convert(converter = UuidBinaryConverter.class)
    private UUID id;

    @Enumerated
    private TemplateTypeEnum templateType;

    private String messagePrefix;

    @ManyToOne
    @JoinColumn(name = "send_request_id", nullable = false)
    private SendRequest sendRequest;

    public static SendMessage of(SendRequest sendRequest, EnumMapperValue templateType, String messagePrefix) {
        SendMessage message = new SendMessage();
        message.assignSendRequest(sendRequest);
        message.assignTemplateTypeAndPrefix(templateType, messagePrefix);
        return message;
    }

    private void assignTemplateTypeAndPrefix(EnumMapperValue templateType, String messagePrefix) {
        TemplateTypeEnum templateTypeCode = resolveTemplateType(templateType);

        if(TemplateTypeEnum.ADVERTISE == templateTypeCode) {
            ValidationUtils.isEmpty("message_prefix", messagePrefix);
            this.messagePrefix = messagePrefix;
        }
    }

    private TemplateTypeEnum resolveTemplateType(EnumMapperValue templateType) {
        TemplateTypeEnum templateTypeCode = Optional.of(templateType)
                .map(type -> TemplateTypeEnum.valueOf(type.code())).orElse(TemplateTypeEnum.NONE);
        this.templateType = templateTypeCode;
        return templateTypeCode;
    }

    private void assignSendRequest(SendRequest sendRequest) {
        if(sendRequest == null) {
            throw SendRequestNotFoundException.of();
        }
        this.sendRequest = sendRequest;
    }

    public String generatePhraseByMessageType(String content) {
        if(templateType == TemplateTypeEnum.ADVERTISE) {
            return "%s %s".formatted(messagePrefix, content);
        }
        return content;
    }
}
